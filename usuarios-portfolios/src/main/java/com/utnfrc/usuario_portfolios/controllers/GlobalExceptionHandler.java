package com.utnfrc.usuario_portfolios.controllers;

import com.utnfrc.usuario_portfolios.excepciones.BilleteraExistenteException;
import com.utnfrc.usuario_portfolios.excepciones.ErrorResponse;
import com.utnfrc.usuario_portfolios.excepciones.SaldoInsuficienteException;
import com.utnfrc.usuario_portfolios.excepciones.TransaccionInversionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Operación Inválida",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BilleteraExistenteException.class)
    public ResponseEntity<ErrorResponse> handleBilleteraExistente(BilleteraExistenteException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Operacion Invalida",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransaccionInversionException.class)
    public ResponseEntity<ErrorResponse> handleTransaccionInversion(TransaccionInversionException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Operacion Invalida",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}