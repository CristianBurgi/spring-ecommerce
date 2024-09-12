package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Orden;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IOrdenService {
    //metodos para la gestion de ordenes de compra
    @Transactional
    Orden save(Orden orden);
    List<Orden> findAll();
    String generaNumeroOrden();

}
