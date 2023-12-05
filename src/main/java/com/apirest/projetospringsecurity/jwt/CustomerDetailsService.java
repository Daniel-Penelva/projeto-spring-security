package com.apirest.projetospringsecurity.jwt;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apirest.projetospringsecurity.daos.UsuarioDao;
import com.apirest.projetospringsecurity.entities.Usuario;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;

    private Usuario userDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loadUserByUsername {}", username);

        userDetails = usuarioDao.findByEmail(username);

        if (!Objects.isNull(userDetails)) {
            return new org.springframework.security.core.userdetails.User(userDetails.getEmail(),
                    userDetails.getPassword(), new ArrayList<>());

        } else {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

    public Usuario getUserDetail() {
        return userDetails;
    }

}
