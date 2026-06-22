package com.example.yahooapifinance.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class StockController {

	@GetMapping("/")
	public String saludo() {
		return "Hola mundo!";
	}
}
