package com.example.downlord.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;

/************************
 * Made by [MR Ferry™]  *
 * on Desember 2024     *
 ************************/

@ControllerAdvice
public class AdviceController{

	@ExceptionHandler(FileNotFoundException.class)
	public String handleFileNotFoundException(FileNotFoundException e, Model model) {
		model.addAttribute("error", "File or folder at '/home/" + e.getMessage() + "' does not exist or is not readable");
		return "FileList";
	}

}
