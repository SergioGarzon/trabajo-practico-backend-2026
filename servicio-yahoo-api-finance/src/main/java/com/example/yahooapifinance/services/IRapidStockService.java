package com.example.yahooapifinance.services;

import java.math.BigDecimal;

public interface IRapidStockService {

	BigDecimal getStockDataFromRapidApi(String symbol);
}
