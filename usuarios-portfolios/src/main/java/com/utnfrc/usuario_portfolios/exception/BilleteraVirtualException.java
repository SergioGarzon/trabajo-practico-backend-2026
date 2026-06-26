package com.utnfrc.usuario_portfolios.exception;


public class BilleteraVirtualException extends RuntimeException {
    public BilleteraVirtualException(String message) {
        super(message);
    }

    public BilleteraVirtualException(String message, Throwable cause) {
        super(message, cause);
    }
}
