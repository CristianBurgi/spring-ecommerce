package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    public Producto save (Producto producto);
    public Optional<Producto> get(Long id);
    public void update (Producto producto);
    public void delete (Long id);
    public List<Producto> findAll();
}
