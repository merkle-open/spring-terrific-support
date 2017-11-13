package com.namics.oss.spring.support.terrific.starter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HomeController.
 *
 * @author bhelfenberger, Namics AG
 * @since 02.10.17 13:21
 */
@Controller
public class HomeController {

	@RequestMapping(value = { "/" })
	public String redirect(Model model) {
		return "redirect:/users";
	}

}
