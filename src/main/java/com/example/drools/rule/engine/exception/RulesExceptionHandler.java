package com.example.drools.rule.engine.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.drools.rule.engine.model.RulesResponse;

@RestControllerAdvice
public class RulesExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RulesCustomException.class)
	protected ResponseEntity<RulesResponse> handleCustomException(RulesCustomException ex, HttpServletRequest request,
			HttpServletResponse response) {

		RulesResponse res = new RulesResponse();
		res.setStatus("Failure");
		res.setMessage(ex.getMessage());
		res.setRuleMessage("Unable to trigger rules");
		res.setSelfApproval(false);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}
		

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<RulesResponse> handleException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {

		RulesResponse res = new RulesResponse();
		res.setStatus("Failure");
		res.setMessage("Expection Occured");
		res.setRuleMessage("Unable to trigger rules");
		res.setSelfApproval(false);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
}
