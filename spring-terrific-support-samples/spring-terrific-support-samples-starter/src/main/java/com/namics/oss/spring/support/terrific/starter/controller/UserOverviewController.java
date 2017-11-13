package com.namics.oss.spring.support.terrific.starter.controller;/*
 * Copyright 2000-2013 Namics AG. All rights reserved.
 */

import com.namics.oss.spring.support.terrific.starter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * UserOverviewController.
 *
 * @author aschaefer, Namics AG
 * @since 22.05.2013
 */
@Controller
public class UserOverviewController {

	@Inject
	private UserService userService;

	@RequestMapping(value = "/users")
	public String view(Model model) {
		model.addAttribute("users", this.userService.getUsers());
		return "views/users-overview";
	}

}
