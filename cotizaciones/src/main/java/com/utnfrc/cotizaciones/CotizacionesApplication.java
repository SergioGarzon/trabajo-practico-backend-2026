package com.utnfrc.cotizaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CotizacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotizacionesApplication.class, args);
	}
}
