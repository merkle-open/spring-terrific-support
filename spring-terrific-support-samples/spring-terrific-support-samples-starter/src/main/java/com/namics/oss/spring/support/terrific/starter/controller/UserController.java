package com.namics.oss.spring.support.terrific.starter.controller;/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

import com.namics.oss.spring.support.terrific.starter.model.User;
import com.namics.oss.spring.support.terrific.starter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * UserController.
 *
 * @author aschaefer, Namics AG
 * @since 22.05.2013
 */
@Controller
public class UserController {

	@Inject
	private UserService userService;

	@RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
	public String form(@ModelAttribute("user") User user) {
		return "views/users-form";
	}

	@RequestMapping(value = "/users/{username}", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("user") User user,
	                     BindingResult errors,
	                     Model model) {
		if (errors.hasErrors()) {
			return this.form(user);
		}
		this.userService.saveUser(user);
		return "redirect:/users";
	}

	@ModelAttribute("user")
	public User loadModel(@PathVariable("username") String username) {
		return this.userService.getUser(username);
	}

	@InitBinder("user")
	public void initBinderModel(WebDataBinder binder) {
		binder.setDisallowedFields("username");
	}

}
