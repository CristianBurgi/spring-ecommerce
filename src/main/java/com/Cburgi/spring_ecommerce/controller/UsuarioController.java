package com.Cburgi.spring_ecommerce.controller;

import com.Cburgi.spring_ecommerce.model.Orden;
import com.Cburgi.spring_ecommerce.model.Usuario;
import com.Cburgi.spring_ecommerce.service.IOrdenService;
import com.Cburgi.spring_ecommerce.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private IUsuarioService userService;

    @Autowired
    private IOrdenService ordenService;


    @GetMapping("/registro")
    public String create() {

        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario) {
       logger.info("Usuario registro: {} " + usuario);
       usuario.setTipo("USER");
       userService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {

        return "usuario/login";
    }

    @PostMapping("/acceder")
    public String acceso(Usuario usuario, HttpSession session) {
        logger.info("Usuario acceso: {} " + usuario);

        Optional<Usuario> user = userService.findByEmail(usuario.getEmail());
        //logger.info("Usuario en db: {} " + user.get().toString());

        if (user.isPresent()) {
            session.setAttribute("idusuario", user.get().getId());
            if (user.get().getTipo().equals("ADMIN")) {
                return "redirect:/administrador";
            }else{
                return "redirect:/";
            }

        }else{
            logger.info("Usuario no existe");
        }

        return "redirect:/";
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion",session.getAttribute("idusuario"));

        Usuario usuario = userService.findPorId(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);
        model.addAttribute("ordenes",ordenes);

        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(Model model, @PathVariable Long id, HttpSession session) {
       logger.info("id de la orden : {}", id);
        //orden
        Optional<Orden> orden = ordenService.findById(id);

        model.addAttribute("detalles",orden.get().getDetalle());

        //session
        model.addAttribute("sesion",session.getAttribute("idusuario"));

        return "usuario/detalleCompra";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("idusuario");
        return "redirect:/";
    }

}
