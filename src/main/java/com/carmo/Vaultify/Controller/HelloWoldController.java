package com.carmo.Vaultify.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWoldController {
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

}
