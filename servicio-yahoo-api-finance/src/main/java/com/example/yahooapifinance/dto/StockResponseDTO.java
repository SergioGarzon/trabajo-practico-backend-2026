package com.example.yahooapifinance.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDTO {
    private BigDecimal priceArs;
}
