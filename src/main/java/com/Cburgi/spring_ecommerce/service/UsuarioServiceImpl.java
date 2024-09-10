package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private IUsuarioRepository usuarioRepository;




    @Override
    public Optional findPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


}
