package com.namics.oss.spring.support.terrific.demo.service;

import com.namics.oss.spring.support.terrific.demo.model.User;

import java.util.Collection;

public interface UserService {

	public Collection<User> getUsers();

	public User getUser(String username);

	public void saveUser(User user);

}