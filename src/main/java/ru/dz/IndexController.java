package ru.dz;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Aydar Farrakhov on 06.12.16.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
