package com.namics.oss.spring.support.terrific.starter.model;/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

import org.hibernate.validator.constraints.NotBlank;

/**
 * User.
 *
 * @author aschaefer, Namics AG
 * @since demo 2.0 22.05.2013
 */
public class User {

	@NotBlank
	private String username;

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;

	/**
	 * User.
	 */
	public User() {
		super();
	}

	/**
	 * User.
	 *
	 * @param username
	 * @param firstname
	 * @param lastname
	 */
	public User(String username, String firstname, String lastname) {
		super();
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	/**
	 * Getter for firstname. @return the firstname
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * Setter for firstname. @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * Getter for lastname. @return the lastname
	 */
	public String getLastname() {
		return this.lastname;
	}

	/**
	 * Setter for lastname. @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Getter for username. @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Setter for username. @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
