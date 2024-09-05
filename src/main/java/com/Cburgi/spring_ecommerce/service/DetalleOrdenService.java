package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.DetalleOrden;
import com.Cburgi.spring_ecommerce.repository.IDetalleOrdenRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrdenService  implements IDetalleOrdenService{

    @Autowired
    private IDetalleOrdenRepository detalleOrdenRepository;

    @Override
    public DetalleOrden save(DetalleOrden detalleOrden) {
        return detalleOrdenRepository.save(detalleOrden);
    }
}
