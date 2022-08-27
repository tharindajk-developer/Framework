/*
 *ABC Bank 2022
 */
package com.abcbank.backend.util;

/*
 *tharinda.jayamal@gmail.com
 */
public class ResponseModel {

	private String code;
	private String message;
	
	public ResponseModel(String message, String code) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
