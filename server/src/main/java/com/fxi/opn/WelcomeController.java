package com.fxi.opn;

/**
 * Created by seki on 18/6/12.
 */
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/index")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "index";
    }

}
