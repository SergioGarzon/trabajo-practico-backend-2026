package com.catedra.backend.compraventa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompraVentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompraVentaApplication.class, args);
	}

}
