package com.example.drools.rule.engine.model;

public class RulesResponse {
	
	private String status;
	private String message;
	private String ruleMessage;
	private boolean selfApproval;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRuleMessage() {
		return ruleMessage;
	}
	public void setRuleMessage(String ruleMessage) {
		this.ruleMessage = ruleMessage;
	}
	public boolean isSelfApproval() {
		return selfApproval;
	}
	public void setSelfApproval(boolean selfApproval) {
		this.selfApproval = selfApproval;
	}
	
	

}
