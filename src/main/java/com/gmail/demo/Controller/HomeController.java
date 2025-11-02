package com.gmail.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/home")
	public String homePage(Model model) {
		model.addAttribute("message", "Welcome to Home Page!");
		return "home"; // Thymeleaf template: home.html
	}

	@GetMapping("/login")
	public String login() {
		return "login"; // maps to login.html
	}
}
