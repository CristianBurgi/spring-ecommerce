package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.DetalleOrden;
import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.service.IDetalleOrdenService;
import com.Cburgi.spring_ecommerce.service.IOrdenService;
import com.Cburgi.spring_ecommerce.service.IUsuarioService;
import com.Cburgi.spring_ecommerce.service.IProductoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private IProductoService IProductoService;

    //para almacenar los detalles de la orden
    private List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();

    //datos de la orden
    Orden orden= new Orden();

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private IDetalleOrdenService detalleOrdenService;


    //Metodos......................

    @GetMapping("")
    public String home(Model model, HttpSession session){

        log.info("Home con sesion activa usuario {}",session.getAttribute("idusuario"));
        model.addAttribute("productos", IProductoService.findAll());
        // session
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Long id, Model model) {
        log.info("Id enviado cpmp párametro {}", id);

        Producto producto = new Producto();
        Optional<Producto> optionalProducto = IProductoService.get(id);
        producto = optionalProducto.get();
        model.addAttribute("producto", producto);  // se pasa el producto al modelo para mostrarlo en la vista

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addToCart(@RequestParam Long id, @RequestParam Double cantidad, Model model) {

        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = IProductoService.get(id);
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
    public String getCart(Model model, HttpSession session) {

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        //sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {

        Usuario usuario = usuarioService.findPorId(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);


        return "usuario/resumenorden";
    }


    //Metodo para guardar la orden
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session) {
        try {
            // Crear una nueva instancia de Orden
            Orden orden = new Orden();
            Date fechaCreacion = new Date();
            orden.setFechaCreacion(fechaCreacion);
            orden.setNumero(ordenService.generaNumeroOrden());

            // Obtener y setear usuario
            Usuario usuario = usuarioService.findPorId(Integer.parseInt(session.getAttribute("idusuario").toString())).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            orden.setUsuario(usuario);

            // Guardar orden
            ordenService.save(orden);

            // Guardar detalles de la orden
            for (DetalleOrden detalleOrden : detalles) {
                detalleOrden.setOrden(orden);
                detalleOrdenService.save(detalleOrden);
            }

            // Vaciar carrito
            detalles.clear();
            orden.setTotal(0.0);

            log.info("Orden guardada con éxito");

        } catch (Exception e) {
            log.error("Error al guardar la orden", e);
            return "error"; // Redirige a una página de error o muestra un mensaje adecuado
        }

        return "redirect:/";
    }



    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model){
        log.info("nombre del producto : {}", nombre);
        List<Producto> productos = IProductoService.findAll().stream().
                filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
               .collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }


}
