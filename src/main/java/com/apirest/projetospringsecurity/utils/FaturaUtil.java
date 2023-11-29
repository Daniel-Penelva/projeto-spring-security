package com.apirest.projetospringsecurity.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FaturaUtil {

    private FaturaUtil() {

    }

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus) {
        return new ResponseEntity<String>("Mensagem: " + message, httpStatus);
    }
}
