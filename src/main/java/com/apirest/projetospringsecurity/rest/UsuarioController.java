package com.apirest.projetospringsecurity.rest;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.projetospringsecurity.constants.FaturaConstant;
import com.apirest.projetospringsecurity.services.UsuarioService;
import com.apirest.projetospringsecurity.utils.FaturaUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private UsuarioService usuarioService;

    // localhost:8081/api/usuarios/signup
    @PostMapping("/signup")
    public ResponseEntity<String> registrarUsuario(@RequestBody(required = true) Map<String, String> requestMap){
        try {
            return usuarioService.signUp(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FaturaUtil.getResponseEntity(FaturaConstant.SOMENTHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // localhost:8081/api/usuarios/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap){

        try {
            return usuarioService.login(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FaturaUtil.getResponseEntity(FaturaConstant.SOMENTHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*OBS. Ao testar o método 'login()' no postman vai gerar um token, no caso copia o token gerado, acesse  o site 'https://jwt.io/' e cole 
    esse token no campo "Encoded" para gerar o playoad. */ 
    
}
