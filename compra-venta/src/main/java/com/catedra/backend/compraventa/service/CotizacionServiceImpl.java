package com.catedra.backend.compraventa.service;

import com.catedra.backend.compraventa.client.CotizacionClient;
import com.catedra.backend.compraventa.exception.CotizacionNoEncontradaException;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Primary
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private static final Double TIPO_CAMBIO_USD_ARS = 1200.0;

    private final CotizacionClient cotizacionClient;
    private final ObjectMapper objectMapper;

    @Override
    public Double obtenerPrecioPorSimbolo(String simbolo) {
        try {
            String jsonCrudo = cotizacionClient.obtenerCotizacionCruda(simbolo.toUpperCase());
            Double precioUsd = extraerPrecioDeJson(jsonCrudo);
            return precioUsd * TIPO_CAMBIO_USD_ARS;
        } catch (CotizacionNoEncontradaException e) {
            throw e;
        } catch (Exception e) {
            throw new CotizacionNoEncontradaException(simbolo);
        }
    }

    @Override
    public boolean existeSimbolo(String simbolo) {
        try {
            obtenerPrecioPorSimbolo(simbolo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Double extraerPrecioDeJson(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode regularMarketPrice = root
                    .path("optionChain")
                    .path("result")
                    .path(0)
                    .path("quote")
                    .path("regularMarketPrice");

            if (!regularMarketPrice.isMissingNode() && regularMarketPrice.isNumber()) {
                return regularMarketPrice.asDouble();
            }

            JsonNode altPrice = root.path("regularMarketPrice");
            if (!altPrice.isMissingNode() && altPrice.isNumber()) {
                return altPrice.asDouble();
            }

            JsonNode priceNode = root.path("price");
            if (!priceNode.isMissingNode() && priceNode.isNumber()) {
                return priceNode.asDouble();
            }

            throw new RuntimeException("No se encontro el campo de precio en la respuesta.");

        } catch (Exception e) {
            throw new RuntimeException("Error al parsear la respuesta de cotizacion: " + e.getMessage());
        }
    }
}
