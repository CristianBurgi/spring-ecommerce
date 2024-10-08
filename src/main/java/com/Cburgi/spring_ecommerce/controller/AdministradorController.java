package com.Cburgi.spring_ecommerce.controller;


import com.Cburgi.spring_ecommerce.model.Producto;
import com.Cburgi.spring_ecommerce.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administrador")
//@PreAuthorize("hasRole('ADMIN')") // requires user to have ADMIN role to access this controller
//@AllArgsConstructor
public class AdministradorController {
    @Autowired
    private IProductoService IProductoService;

    @GetMapping("")
    public String home(Model model) {

        List<Producto> productos = IProductoService.findAll();
        model.addAttribute("productos", productos);

        return "administrador/home.html";
    }
}
