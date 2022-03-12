package com.example.drools.rule.engine.delegate;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.drools.rule.engine.model.RulesRequestDetails;
import com.example.drools.rule.engine.model.RulesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RulesDelegate {

	private static final Logger logger = LoggerFactory.getLogger("RulesDelegate");

	@Autowired
	private KieSession ksession;

	public RulesResponse execute(RulesRequestDetails request) {

		RulesResponse response = new RulesResponse();

		ksession.insert(request);
		ksession.insert(response);

		int rulesExecuted = ksession.fireAllRules();
		if (rulesExecuted == 0) {
			response.setStatus("Success");
			response.setMessage("No rules have been triggered");
		}

		ksession.dispose();

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

	/*
	 * public RulesResponse fireRules(RulesRequestDetails request) {
	 * 
	 * RulesResponse response = new RulesResponse();
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); String input = ""; try { input =
	 * mapper.writeValueAsString(request); } catch (JsonProcessingException e) {
	 * 
	 * logger.error("Error in parsing Json request : {} ", e.getMessage()); }
	 * 
	 * logger.info("json request : {}", input);
	 * 
	 * KieServices kieServices = KieServices.Factory.get(); KieContainer kContainer
	 * = kieServices.getKieClasspathContainer(); KieSession ksession =
	 * kContainer.newKieSession("kiesession"); ksession.insert(request);
	 * ksession.insert(response); int rulesExecuted = ksession.fireAllRules(); if
	 * (rulesExecuted == 0) { response.setStatus("Success");
	 * response.setMessage("No rules have been triggered"); }
	 * 
	 * ksession.dispose(); return response;
	 * 
	 * }
	 */
}
