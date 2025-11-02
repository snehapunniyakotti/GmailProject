package com.gmail.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmail.demo.entity.User;
import com.gmail.demo.repository.UserRepository;
import com.gmail.demo.service.security.Google2FAService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class Google2FAController {

	   @Autowired
	    private Google2FAService google2faService;

	    @Autowired
	    private UserRepository userRepository;

	    @GetMapping("/setup-2fa")
	    public String showSetupPage(HttpSession session, Model model) {
	        model.addAttribute("qrUri", session.getAttribute("qrUri"));
	        return "setup-2fa";
	    }

	    @GetMapping("/verify-2fa")
	    public String showVerifyPage() {
	        return "2fa-verification";
	    }

	    @PostMapping("/verify")
	    public String verifyCode(@RequestParam String code, HttpSession session) {
	    	System.out.println(" request code : "+code);
	        String email = (String) session.getAttribute("email");
	        String secret = (String) session.getAttribute("secret");
	        User user = userRepository.findByEmail(email);
	        System.out.println(" session email "+ email);
	        System.out.println(" session  email : "+ email + " session user details : "+ user +" user.getSecretKey() " +user.getSecretKey());
//	        if (user != null && google2faService.verifyCode(user.getSecretKey(), code)) {
	        boolean isvalid = google2faService.verifyCode(secret, code);
	        if(isvalid) {
//	            return "redirect:/home";
	        	return "home";  
	        }
	        return "redirect:/verify-2fa?error=true";
	    }
	    
	    PX2DAHSVIS4XD2HFNL3K2AFSVOFIPQIS
}
