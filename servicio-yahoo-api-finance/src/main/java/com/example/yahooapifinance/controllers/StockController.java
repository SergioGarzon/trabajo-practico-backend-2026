package com.example.yahooapifinance.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.yahooapifinance.services.StockService;

import yahoofinance.Stock;


@RestController
@RequestMapping("/api/stocks")
public class StockController {

	private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public Stock getStock(@PathVariable String symbol) {
        return stockService.getStockDetails(symbol.toUpperCase());
    }
}
