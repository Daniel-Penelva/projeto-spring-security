package com.apirest.projetospringsecurity.servicesImpls;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.apirest.projetospringsecurity.constants.FaturaConstant;
import com.apirest.projetospringsecurity.daos.UsuarioDao;
import com.apirest.projetospringsecurity.entities.Usuario;
import com.apirest.projetospringsecurity.jwt.CustomerDetailsService;
import com.apirest.projetospringsecurity.jwt.JwtUtil;
import com.apirest.projetospringsecurity.services.UsuarioService;
import com.apirest.projetospringsecurity.utils.FaturaUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioDao usuarioDao;

    private AuthenticationManager authenticationManager;

    private CustomerDetailsService customerDetailsService;

    private JwtUtil jwtUtil;

    // Este método é usado para lidar com o processo de registro de usuários.
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Registro interno de um usuário {}", requestMap);

        try {
            if (validateSignUpMap(requestMap)) {
                Usuario user = usuarioDao.findByEmail(requestMap.get("email"));

                if (Objects.isNull(user)) {
                    usuarioDao.save(getUserFromMap(requestMap));
                    System.out.println("TESTE - VERIFICAR USUÁRIO CADASTRADO");
                    return FaturaUtil.getResponseEntity("Usuário cadastrado com Sucesso!", HttpStatus.CREATED);

                } else {
                    return FaturaUtil.getResponseEntity("O usuário com esse email já existe", HttpStatus.BAD_REQUEST);
                }
            } else {
                return FaturaUtil.getResponseEntity(FaturaConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return FaturaUtil.getResponseEntity(FaturaConstant.SOMENTHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Valida se o mapa de solicitação contém as chaves necessárias.
    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("nome") && requestMap.containsKey("telefone")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }

        return false;
    }

    // Cria e retorna um objeto `Usuario` com base no mapa de solicitação.
    private Usuario getUserFromMap(Map<String, String> requestMap) {

        Usuario user = new Usuario();

        user.setNome(requestMap.get("nome"));
        user.setTelefone(requestMap.get("telefone"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("false"));
        user.setRole(requestMap.get("user"));

        return user;
    }

    // Método de autenticação de login para a aplicação Spring Security que utiliza tokens JWT
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {

        log.info("Dentro de login");

        try {

            // Autentica o usuário usando o AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            // Verifica se a autenticação foi bem-sucedida
            if (authentication.isAuthenticated()) {
                // Verifica o status do usuário
                if (customerDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    // Gera um token JWT e retorna uma resposta bem-sucedida
                    return new ResponseEntity<String>(
                            "{\"token\":\"" + jwtUtil.generateToken(customerDetailsService.getUserDetail().getEmail(),
                                    customerDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);

                } else {
                    // Retorna uma resposta de acesso não aprovado pelo administrador
                    return new ResponseEntity<String>("{\"mensagem\":\"" + " Acesso não aprovado pelo administrador " + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }

        } catch (Exception exception) {
            // Loga qualquer exceção ocorrida durante o processo de autenticação
            log.error("{}", exception);
        }

        // Retorna uma resposta de credenciais incorretas
        return new ResponseEntity<String>("{\"mensagem\":\"" + " Credenciais incorretas " + "\"}",
                HttpStatus.BAD_REQUEST);
    }

}
