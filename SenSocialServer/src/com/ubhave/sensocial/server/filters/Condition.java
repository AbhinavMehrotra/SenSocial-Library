package com.ubhave.sensocial.server.filters;

public class Condition {
	String modalityType="";
	String operator="";
	String modalValue="";
	String condition="";

	public Condition(String modalityType, String operator, String modalValue){
		this.modalityType=modalityType;
		this.modalValue=modalValue;
		this.operator=operator;
		this.condition= modalityType+operator+modalValue;
	}

	public Condition(){

	}

	public Condition(String fullConditionString){
		//parse the string and set all values
		this.modalityType=fullConditionString.substring(0, fullConditionString.indexOf(":"));
		this.operator=fullConditionString.substring(fullConditionString.indexOf(":")+1, fullConditionString.lastIndexOf(":"));
		this.modalValue=fullConditionString.substring(fullConditionString.lastIndexOf(":")+1, fullConditionString.length());
	}

	/**
	 * @return the modalityType
	 */
	public String getModalityType() {
		return modalityType;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return the modalValue
	 */
	public String getModalValue() {
		return modalValue;
	}

	/**
	 * @return the condition
	 */
	public String getConditionString() {
		return condition;
	}


}
