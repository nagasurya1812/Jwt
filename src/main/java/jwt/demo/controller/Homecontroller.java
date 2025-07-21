package jwt.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class Homecontroller {
    @GetMapping("/home")
    public String Home() {
    	return "welcome home page";
    }
    @GetMapping("/dashboard")
    public String Dashboard() {
    	return "welcome Dashboard";
    }
}
