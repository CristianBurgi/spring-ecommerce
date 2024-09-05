package com.Cburgi.spring_ecommerce.repository;

import com.Cburgi.spring_ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto,Long> {



}
