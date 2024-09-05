package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {
    // Implementación de métodos para el manejo de usuarios
    // Por ejemplo: guardar, actualizar, eliminar, etc.
    Optional<Usuario> findPorId(Integer id);
}
