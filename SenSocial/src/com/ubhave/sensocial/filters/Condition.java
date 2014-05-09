/*******************************************************************************
 *
 * SenSocial Middleware
 *
 * Copyright (c) ${2014}, University of Birmingham
 * Abhinav Mehrotra, axm514@cs.bham.ac.uk
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Birmingham 
 *       nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
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
