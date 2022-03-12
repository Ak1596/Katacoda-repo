package com.example.drools.rule.engine.util;

import org.springframework.stereotype.Component;

import com.example.drools.rule.engine.exception.RulesCustomException;
import com.example.drools.rule.engine.model.RulesRequestDetails;

@Component
public class RequestValidator  {

	public void rulesRequestValidator(RulesRequestDetails request) {

		if (request.getEmpId() == null || request.getEmpId().isEmpty()) {
			throw new RulesCustomException("Employee ID cannot be emtpty or null");
		}

	}

}
