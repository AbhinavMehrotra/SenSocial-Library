package com.ubhave.sensocial.filters;

/**
 * Condition class provide creation of filter-conditions
 */
public class Condition {
	String modalityType="";
	String operator="";
	String modalValue="";
	String condition="";

	/**
	 * Constructor
	 * @param String modality-type
	 * @param String operator
	 * @param String modalValue
	 */
	public Condition(String modalityType, String operator, String modalValue){
		this.modalityType=modalityType;
		this.modalValue=modalValue;
		this.operator=operator;
		this.condition= modalityType+operator+modalValue;
	}

	/**
	 * Constructor
	 * @param String Condition-String
	 */
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
	 * Getter for modality type
	 * @return the modalityType
	 */
	public String getModalityType() {
		return modalityType;
	}

	/**
	 * Getter for operator
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Getter for modal value
	 * @return the modalValue
	 */
	public String getModalValue() {
		return modalValue;
	}

	/**
	 * Getter for condition string
	 * @return the condition
	 */
	public String getConditionString() {
		return condition;
	}


}
