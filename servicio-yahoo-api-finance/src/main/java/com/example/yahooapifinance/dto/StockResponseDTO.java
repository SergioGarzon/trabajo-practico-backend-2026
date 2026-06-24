package com.example.yahooapifinance.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDTO {
	private String symbol;
    private String name;
    private BigDecimal priceArs;
}
