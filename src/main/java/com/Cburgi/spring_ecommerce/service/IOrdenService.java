package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Orden;

import java.util.List;

public interface IOrdenService {
    //metodos para la gestion de ordenes de compra
    Orden save(Orden orden);
    List<Orden> findAll();

}
