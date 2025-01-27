package com.developkim.rabbitmq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
//@RestController
@Controller
public class HelloController {

    @GetMapping
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }

    @GetMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("message", "Welcome to RabbitMQ Sample!");
        return "home";
    }
}
