package com.Cburgi.spring_ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
//@PreAuthorize("hasRole('ADMIN')") // requires user to have ADMIN role to access this controller
//@AllArgsConstructor
public class AdministradorController {

    @GetMapping("")
    public String home(){
        return "administrador/home.html";
    }
}
