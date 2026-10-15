package com.carmo.Vaultify.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
public class HelloWoldController {
    @GetMapping("/")
    public String helloWorld() {
        return "index";
    }

}
