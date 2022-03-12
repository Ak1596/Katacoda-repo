package com.example.drools.rule.engine.util;

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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.example.drools.rule.engine.service.RuleService;

@Service
public class RulesUtil {

	private static final Logger logger = LoggerFactory.getLogger("RulesUtil");

	@Value("${rules.upload.base.url}")
	private String rulesPath;

	@Autowired
	private RuleService ruleService;
	
	private KieBase kbase;
	private KieFileSystem kfilesystem;

	@Bean
	public KieSession getKsession() {
		return kbase.newKieSession();
	}

	@PostConstruct
	public void init() {

		KieServices ks = KieServices.Factory.get();
		kfilesystem = ks.newKieFileSystem();
		ruleService.cloneRepositry();
		this.build();
		KieBuilder builder = ks.newKieBuilder(kfilesystem);
		builder.buildAll();

		if (builder.getResults().hasMessages(Level.ERROR)) {
			logger.error("Error in building rules : {}", builder.getResults().toString());
		}

		KieContainer container = ks.newKieContainer(builder.getKieModule().getReleaseId());
		kbase = container.getKieBase();

	}

	public void build() {

		File[] rules = new File(rulesPath).listFiles();

		if (rules.length > 0 && rules != null) {
			for (File rule : rules) {
				kfilesystem.write(ResourceFactory.newFileResource(rule));
			}
		}
	}

}
