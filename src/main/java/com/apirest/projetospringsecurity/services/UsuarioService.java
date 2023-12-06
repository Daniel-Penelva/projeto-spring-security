package com.apirest.projetospringsecurity.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    ResponseEntity<String> signUp(Map<String, String> requestMap );

    ResponseEntity<String> login(Map<String, String> requestMap);
    
}
