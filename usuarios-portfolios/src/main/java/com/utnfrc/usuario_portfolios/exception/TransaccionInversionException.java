package com.utnfrc.usuario_portfolios.exception;


public class TransaccionInversionException extends RuntimeException {
    public TransaccionInversionException(String message) {
        super(message);
    }

    public TransaccionInversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
