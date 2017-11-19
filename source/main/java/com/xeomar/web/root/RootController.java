package com.xeomar.web.root;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class RootController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
