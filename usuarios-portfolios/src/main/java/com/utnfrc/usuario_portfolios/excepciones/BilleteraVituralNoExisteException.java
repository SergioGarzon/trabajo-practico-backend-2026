package com.utnfrc.usuario_portfolios.excepciones;

public class BilleteraVituralNoExisteException extends RuntimeException {
    public BilleteraVituralNoExisteException(String message) {
        super(message);
    }
}
