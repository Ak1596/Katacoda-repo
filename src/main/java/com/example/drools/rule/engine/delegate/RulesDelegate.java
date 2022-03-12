package com.example.drools.rule.engine.delegate;

import java.io.File;

import javax.annotation.PostConstruct;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.drools.rule.engine.model.RulesRequestDetails;
import com.example.drools.rule.engine.model.RulesResponse;
import com.example.drools.rule.engine.service.RuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RulesDelegate {

	private static final Logger logger = LoggerFactory.getLogger("RulesDelegate");

	@Value("${rules.clone.base.url}")
	private String rulesPath;
	
	@Value("${rules.folder.base.url}")
	private String location;
	
	@Autowired
	private RuleService service;

	private KieBase kbase;
	private KieFileSystem kfilesystem;

	@PostConstruct
	public void init() {

		KieServices ks = KieServices.Factory.get();
		kfilesystem = ks.newKieFileSystem();
		service.cloneRepositry();
		build();
		KieBuilder builder = ks.newKieBuilder(kfilesystem);
		builder.buildAll();

		if (builder.getResults().hasMessages(Level.ERROR)) {
			logger.error("Error in building rules : {}", builder.getResults().toString());
		}

		KieContainer container = ks.newKieContainer(builder.getKieModule().getReleaseId());
		kbase = container.getKieBase();

	}

	public void build() {
        
		File path = new File(rulesPath + location);
		File[] rules = path.listFiles();

		if (rules.length > 0 && rules != null) {
			for (File rule : rules) {
				if (rule.getName().endsWith(".drl") || rule.getName().endsWith(".xlxs")) {
					logger.info("Starting to upload rule : {}", rule.getName());
					kfilesystem.write(ResourceFactory.newFileResource(rulesPath + location + rule.getName()));
				}
			}
		}
		
	}

	public RulesResponse execute(RulesRequestDetails request) {

		RulesResponse response = new RulesResponse();
		KieSession ksession = kbase.newKieSession();
		ksession.insert(request);
		ksession.insert(response);

		int rulesExecuted = ksession.fireAllRules();
		if (rulesExecuted == 0) {
			response.setStatus("Success");
			response.setMessage("No rules have been triggered");
		}

		ksession.dispose();
		logRequestAndResponse(request, response);

		return response;
	}

	public void logRequestAndResponse(RulesRequestDetails request, RulesResponse response) {

		ObjectMapper mapper = new ObjectMapper();
		String input = "";
		String output = "";
		try {
			input = mapper.writeValueAsString(request);
			output = mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			logger.error("Error in parsing Json request : {} ", e.getMessage());
		}
		logger.info("json request : {}", input);
		logger.info("json response : {}", output);

	}

}
