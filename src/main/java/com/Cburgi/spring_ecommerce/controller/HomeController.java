package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.DetalleOrden;
import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.service.IUsuarioService;
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

    @Autowired
    private IUsuarioService usuarioService;

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
    public String addToCart(@RequestParam Long id, @RequestParam Double cantidad, Model model) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Producto añadido : {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);

        producto = optionalProducto.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio()*cantidad);
        detalleOrden.setProducto(producto);

        //validar qu el producto no se añada dos veces
        Long idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
        if (!ingresado){
            // si no se añadio, se añade a la lista de detalles
            detalles.add(detalleOrden);

        }



        sumaTotal = detalles.stream()
                .mapToDouble(dt -> dt.getTotal() != null ? dt.getTotal() : 0.0)
                .sum();

        orden.setTotal(sumaTotal);

        model.addAttribute("cart",detalles);
        model.addAttribute("orden",orden);


        return "usuario/carrito";

    }

    // quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductCar(@PathVariable Long id, Model model) {
        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

        //lista nueva de productos
        for (DetalleOrden detalleOrden: detalles){
            if (detalleOrden.getProducto().getId() != id){
                ordenesNueva.add(detalleOrden);
            }
        }
        //nueva Lista con los productos restantes
        detalles = ordenesNueva;

        double sumaTotal = 0;
        sumaTotal = detalles.stream()
                .mapToDouble(dt -> dt.getTotal() != null ? dt.getTotal() : 0.0)
                .sum();

        orden.setTotal(sumaTotal);

        model.addAttribute("cart",detalles);
        model.addAttribute("orden",orden);


        return "usuario/carrito";
    }


    @GetMapping("/getCart")
    public String getCart(Model model) {

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model) {

        Usuario usuario = usuarioService.findPorId(1).get();

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);


        return "usuario/resumenorden";
    }


}
