package com.Cburgi.spring_ecommerce.service;

import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.repository.IOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrdenServiceImpl implements IOrdenService {
    @Autowired
    private IOrdenRepository ordenRepository;

    @Override
    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }

    @Override
    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    public String generaNumeroOrden(){
        int numero = 0;
        String numeroConcatenado = "";

        //Obtener el ultimo numero de orden
        List<Orden> ordenes = findAll();

        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.stream().forEach(orden ->numeros.add(Integer.parseInt(orden.getNumero())) );

        if (ordenes.isEmpty()){
            numero = 1;
        }else{
            numero = Collections.max(numeros);
            numero++;
        }


        //revisar esto por las dudas este dando error
        

        //Convertir numero a String con formato 000000000
        numeroConcatenado = String.format("%09d", numero);
        //Agregar el prefijo "000000"
        numeroConcatenado = "000000" + numeroConcatenado;

        //Devolver el numero concatenado con el prefijo "000000"
        return numeroConcatenado;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }
}
