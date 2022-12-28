/************************
 * Made by [MR Ferry™]  *
 * on December 2022     *
 ************************/

package com.example.downlord.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorHandlingConroller implements ErrorController{

	@RequestMapping("/error")
	public String handleError(HttpServletResponse response, Model model){
		model.addAttribute("home", "/");
		String errorValue = response.getStatus() == HttpStatus.NOT_FOUND.value() ? "File or folder does not exist" : "ERROR";
		model.addAttribute("error", errorValue);
		return "FileList";
	}

}
