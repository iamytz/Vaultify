package com.carmo.Vaultify.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RenderTemplatesController {
    //EŚTA CLASSE IRA SER UTILIZDA APENAS PARA RENDERIZAR AS PAGINAS HTML

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
