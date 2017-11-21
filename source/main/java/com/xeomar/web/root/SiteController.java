package com.xeomar.web.root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SiteController {

	@RequestMapping( "/" )
	public String index() {
		return "index.html";
	}

}
