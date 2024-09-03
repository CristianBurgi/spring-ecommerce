package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto){
        LOGGER.info("Guardando producto: {}", producto);
        Usuario usuario = new Usuario(1,"","","","","","","");
        producto.setUsuario(usuario);
        productoService.save(producto);
        return "redirect:/productos";
    }

   /* @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();

        LOGGER.info("Producto buscado : {}", producto);
        return "productos/edit";

    }
    */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        // Intenta obtener el producto con el ID proporcionado
        Optional<Producto> optionalProducto = productoService.get(id);

        // Verifica si el producto está presente
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            // Añade el producto al modelo para la vista
            model.addAttribute("producto", producto);

            // Log para información de depuración
            LOGGER.info("Producto buscado: {}", producto);

            // Devuelve el nombre de la vista para la edición
            return "productos/edit";
        } else {
            // Manejo del caso cuando el producto no es encontrado
            LOGGER.warn("Producto con ID {} no encontrado", id);

            // Redirige a una página de error o a la lista de productos
            return "redirect:/productos"; // O alguna página de error
        }
    }


   /*
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, Producto producto) {
        LOGGER.info("Actualizando producto con ID: {}", id);
        Optional<Producto> optionalProducto = productoService.get(id);

        if (optionalProducto.isPresent()) {
            Producto productoActualizado = optionalProducto.get();
            productoActualizado.setNombre(producto.getNombre());
            productoActualizado.setPrecio(producto.getPrecio());
            productoActualizado.setCantidad(producto.getCantidad());

            productoService.update(productoActualizado);

            LOGGER.info("Producto actualizado: {}", productoActualizado);

            return "redirect:/productos";
        } else {
            LOGGER.warn("Producto con ID {} no encontrado", id);
            return "redirect:/productos";
        }
    }
    */

    @PostMapping("/update")
    public String update (Producto producto){
        productoService.update(producto);
        return "redirect:/productos";
    }

}




