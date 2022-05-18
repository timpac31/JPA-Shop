package io.timpac.shop.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("shop/main")
	public String home() {
		
		return "main";
	}
}
