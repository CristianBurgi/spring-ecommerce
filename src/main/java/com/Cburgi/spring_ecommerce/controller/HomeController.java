package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.DetalleOrden;
import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;

    //para almacenar los detalles de la orden
    private List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();

    //datos de la orden
    Orden orden= new Orden();

    @GetMapping("")
    public String home(Model model){

        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Long id, Model model) {
        log.info("Id enviado cpmp párametro {}", id);

        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();
        model.addAttribute("producto", producto);  // se pasa el producto al modelo para mostrarlo en la vista

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addToCart(@RequestParam String id, @RequestParam Integer cantidad) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(Long.parseLong(id));
        log.info("Producto añadido : {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);

        return "usuario/carrito";

    }



}
