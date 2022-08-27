/*
 *ABC Bank 2022
 */
package com.abcbank.backend.model;

import java.io.Serializable;
import java.util.List;

import com.abcbank.backend.entity.Role;

/*
 *tharinda.jayamal@gmail.com
 */
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String userName;
	private final List<Role> roles;
	private final String message;

	public JwtResponse(String jwttoken, String userName, List<Role> roles, String message) {
		this.jwttoken = jwttoken;
		this.userName = userName;
		this.roles = roles;
		this.message = message;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public String getUserName() {
		return userName;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public String getMessage() {
		return message;
	}

}