package com.example.yahooapifinance.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class StockDTO {
	
	private String name;
	private BigDecimal price;
	private BigDecimal change;
	private String currency;
	private BigDecimal bid;

}



