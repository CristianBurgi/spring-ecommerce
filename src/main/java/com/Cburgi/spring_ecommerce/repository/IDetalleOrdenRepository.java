package com.Cburgi.spring_ecommerce.repository;

import com.Cburgi.spring_ecommerce.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden,Long> {
}
