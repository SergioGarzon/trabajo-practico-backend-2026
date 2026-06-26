package com.utnfrc.usuario_portfolios.exception;


public class PortfolioException extends RuntimeException {
    public PortfolioException(String message) {
        super(message);
    }

    public PortfolioException(String message, Throwable cause) {
        super(message, cause);
    }
}
