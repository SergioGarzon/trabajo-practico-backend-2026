package com.example.yahooapifinance.services;

import java.util.List;

import com.example.yahooapifinance.models.StockJSON;

public interface IStockServiceJSON {
	public List<StockJSON> getStocks();
}
