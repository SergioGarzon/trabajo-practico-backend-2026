package com.example.yahooapifinance.services;


import java.math.BigDecimal;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.yahooapifinance.dto.StockResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;

@Lazy
@Service
public class RapidStockServiceImpl implements IRapidStockService {
	
private final RestTemplate restTemplate = new RestTemplate();    
private static final BigDecimal COTIZACION_ARS = new BigDecimal("1000.00");

@Override
public BigDecimal getStockDataFromRapidApi(String symbol) {
	
    String cleanSymbol = symbol.toUpperCase();
    String url = "https://rapidapi.com" +
            cleanSymbol + "&lang=en-US&region=US";
            
    HttpHeaders headers = new HttpHeaders();
    headers.set("x-rapidapi-key", "47dc174f4cmsh8332c65ae30cf8bp1e5cc8jsne264a9419954");
    headers.set("x-rapidapi-host", "://rapidapi.com");
    headers.set("Content-Type", "application/json");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    	try {
        	ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        	JsonNode root = response.getBody();

        	JsonNode quoteNode = root.path("optionChain").path("result").get(0).path("quote");

        	if (quoteNode != null && !quoteNode.isMissingNode() && quoteNode.has("regularMarketPrice")) {
            	BigDecimal priceUsd = new BigDecimal(quoteNode.path("regularMarketPrice").asText());
            	return priceUsd.multiply(COTIZACION_ARS);
        	}
        
        	throw new RuntimeException("Precio no encontrado en el JSON");

    	} catch (Exception e) {
    		return COTIZACION_ARS;        
    	}
	}
}