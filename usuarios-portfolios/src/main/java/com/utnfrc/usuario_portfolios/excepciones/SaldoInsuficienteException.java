package com.utnfrc.usuario_portfolios.excepciones;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(String mensaje){
        super(mensaje);
    }
}
