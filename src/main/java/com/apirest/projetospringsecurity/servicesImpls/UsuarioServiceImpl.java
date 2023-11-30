package com.apirest.projetospringsecurity.servicesImpls;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.apirest.projetospringsecurity.constants.FaturaConstant;
import com.apirest.projetospringsecurity.daos.UsuarioDao;
import com.apirest.projetospringsecurity.entities.Usuario;
import com.apirest.projetospringsecurity.services.UsuarioService;
import com.apirest.projetospringsecurity.utils.FaturaUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioDao usuarioDao;

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
        if(requestMap.containsKey("nome") && requestMap.containsKey("telefone")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
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

}
