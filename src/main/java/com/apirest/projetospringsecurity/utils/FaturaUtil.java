package com.apirest.projetospringsecurity.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FaturaUtil {

    private FaturaUtil() {

    }

    // Método estático que retorna um ResponseEntity<String> com base nos parâmetros fornecidos
    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus) {

        // Cria um novo ResponseEntity com a mensagem fornecida e o HttpStatus fornecido
        return new ResponseEntity<String>("Mensagem: " + message, httpStatus);
    }
}
