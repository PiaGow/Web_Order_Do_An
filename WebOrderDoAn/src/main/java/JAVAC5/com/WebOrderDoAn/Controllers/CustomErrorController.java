package JAVAC5.com.WebOrderDoAn.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error/403")
    public String error403() {
        return "Error/403";
    }

    @GetMapping("/error/404")
    public String error404() {
        return "Error/404";
    }

    @GetMapping("/error/500")
    public String error500() {
        return "Error/500";
    }


}