package com.ubhave.sensocial.server.filters;

public class Condition {
	String modalityType="";
	String operator="";
	String modalValue="";
	String condition="";

	/**
	 * Constructor
	 * @param modalityType (String) {@link ModalityType}
	 * @param operator (String) {@link Operator}
	 * @param modalValue (String) {@link ModalValue}
	 */
	public Condition(String modalityType, String operator, String modalValue){
		this.modalityType=modalityType;
		this.modalValue=modalValue;
		this.operator=operator;
		this.condition= modalityType+operator+modalValue;
	}

	public Condition(){

	}

	/**
	 * Constructor
	 * @param (String) Full Condition String
	 */
	protected Condition(String fullConditionString){
		//parse the string and set all values
		this.modalityType=fullConditionString.substring(0, fullConditionString.indexOf(":"));
		this.operator=fullConditionString.substring(fullConditionString.indexOf(":")+1, fullConditionString.lastIndexOf(":"));
		this.modalValue=fullConditionString.substring(fullConditionString.lastIndexOf(":")+1, fullConditionString.length());
	}

	/**
	 * Getter for the modality type
	 * @return (String) Modality Type
	 */
	public String getModalityType() {
		return modalityType;
	}

	/**
	 * Getter for the  operator
	 * @return (String) Operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Getter for the modal value
	 * @return (String) Modal Value
	 */
	public String getModalValue() {
		return modalValue;
	}

	/**
	 * Getter for the full condition string-  include {@link ModalityType}, {@link Operator} and {@link ModalValue}
	 * @return (String) Condition in string format
	 */
	public String getConditionString() {
		return condition;
	}


}
