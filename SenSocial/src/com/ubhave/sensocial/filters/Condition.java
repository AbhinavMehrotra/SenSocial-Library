package com.ubhave.sensocial.filters;

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
		if(fullConditionString.equalsIgnoreCase("null")){
			this.modalityType="null";
			this.operator="";
			this.modalValue="";
		}
		else{
			String str=fullConditionString;
			this.modalityType=fullConditionString.substring(0, str.indexOf(":"));
			str=str.substring(str.indexOf(":")+1);
			this.operator=fullConditionString.substring(0, str.indexOf(":"));
			str=str.substring(str.indexOf(":")+1);
			this.modalValue=str;
		}		
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
