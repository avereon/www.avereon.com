package com.xeomar.web.root;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteController {

	@RequestMapping( "/" )
	public String index() {
		return "Greetings from Spring Boot!";
	}

}
