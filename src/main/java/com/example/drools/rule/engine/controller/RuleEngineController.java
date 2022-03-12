package com.example.drools.rule.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drools.rule.engine.delegate.RulesDelegate;
import com.example.drools.rule.engine.model.RulesRequestDetails;
import com.example.drools.rule.engine.model.RulesResponse;
import com.example.drools.rule.engine.util.RequestValidator;

@RestController
@RequestMapping("/v1/rules")
public class RuleEngineController {

	@Autowired
	RulesDelegate delegate;

	@Autowired
	RequestValidator validator;

	@PostMapping(value = "/execute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RulesResponse executeRules(@RequestBody RulesRequestDetails request) {
		validator.rulesRequestValidator(request);
		return delegate.execute(request);
	}
}
