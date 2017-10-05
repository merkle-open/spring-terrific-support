/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.terrific.demo.service;

import com.namics.oss.spring.support.terrific.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * UserServiceImpl.
 *
 * @author aschaefer, Namics AG
 * @since 22.05.2013
 */
@Service
public class UserServiceImpl implements UserService {
	private Map<String, User> users = new HashMap<String, User>();

	{
		this.users.put("hmuster", new User("hmuster", "Hans", "Muster"));
		this.users.put("jdoe", new User("jdoe", "Jane", "Doe"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<User> getUsers() {
		return this.users.values();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser(String username) {
		return this.users.get(username);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveUser(User user) {
		this.users.put(user.getUsername(), user);
	}
}
