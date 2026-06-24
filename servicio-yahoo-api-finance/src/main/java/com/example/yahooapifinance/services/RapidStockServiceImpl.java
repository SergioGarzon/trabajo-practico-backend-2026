package com.example.yahooapifinance.services;


import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class RapidStockServiceImpl implements IRapidStockService {
	
	private RestTemplate restTemplate;
	
	public RapidStockServiceImpl() {
		restTemplate = new RestTemplate();
	}
	
    public String getStockDataFromRapidApi(String symbol) {
    	
    	String url = "https://yahoo-finance-real-time1.p.rapidapi.com/stock/get-options?symbol=" +
    			symbol.toUpperCase() + "&lang=en-US&region=US";
    	    	
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", "47dc174f4cmsh8332c65ae30cf8bp1e5cc8jsne264a9419954");
        headers.set("x-rapidapi-host", "yahoo-finance-real-time1.p.rapidapi.com");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con RapidAPI: " + e.getMessage());
        }
    }

}
