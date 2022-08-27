/*
 *ABC Bank 2022
 */
package com.abcbank.backend.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 *tharinda.jayamal@gmail.com
 */
@Document
public class LoginHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private Date loggedInAt;
	private Date LoggedOutAt;
	private Login login;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLoggedInAt() {
		return loggedInAt;
	}

	public void setLoggedInAt(Date loggedInAt) {
		this.loggedInAt = loggedInAt;
	}

	public Date getLoggedOutAt() {
		return LoggedOutAt;
	}

	public void setLoggedOutAt(Date loggedOutAt) {
		LoggedOutAt = loggedOutAt;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}
