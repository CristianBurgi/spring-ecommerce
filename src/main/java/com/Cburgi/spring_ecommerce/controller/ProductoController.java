package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.service.IProductoService;
import com.Cburgi.spring_ecommerce.service.IUsuarioService;
import com.Cburgi.spring_ecommerce.service.UploadFileService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private IProductoService IProductoService;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", IProductoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Guardando producto: {}", producto);


        Usuario usuario = usuarioService.findPorId(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(usuario);

        //imagen
        if(producto.getId() == null) {  //validacion de cuando se crea un producto
            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);

        }

        IProductoService.save(producto);

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
        Optional<Producto> optionalProducto = IProductoService.get(id);

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
    public String update (Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
        Producto p = new Producto();
        p= IProductoService.get(producto.getId()).get();


        if (file.isEmpty()){  //cuando editamos un producto pero no cambiamos la imagen
            producto.setImagen(p.getImagen());

        }else{  // cuando se edita tmb la imagen

            if (!p.getImagen().equals("default.jpg")){

                uploadFileService.deleteImage(p.getImagen());  //eliminar imagen del server al eliminar el producto
            }

            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        IProductoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Producto p= new Producto();
        p= IProductoService.get(id).get();

        if (!p.getImagen().equals("default.jpg")){
            uploadFileService.deleteImage(p.getImagen());  //eliminar imagen del server al eliminar el producto
        }

        IProductoService.delete(id);

        return "redirect:/productos";
    }



}




