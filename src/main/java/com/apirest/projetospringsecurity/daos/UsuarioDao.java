package com.apirest.projetospringsecurity.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apirest.projetospringsecurity.entities.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Integer>{

    Usuario findByEmail(@Param("email") String email);
    
}
