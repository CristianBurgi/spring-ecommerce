package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IOrdenService {
    //metodos para la gestion de ordenes de compra
    @Transactional
    Orden save(Orden orden);

    Optional<Orden> findById(Long id);

    List<Orden> findAll();

    String generaNumeroOrden();

    List<Orden> findByUsuario(Usuario usuario);


}
