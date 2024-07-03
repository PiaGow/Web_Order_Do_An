package JAVAC5.com.WebOrderDoAn.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home")
public class HomeController {
    @GetMapping
    public String home(@AuthenticationPrincipal User user, @AuthenticationPrincipal OAuth2User oidcUserService, Model model) {
        if(user != null) {
            model.addAttribute("username", user.getUsername());
        }
        else if(oidcUserService != null) {
            model.addAttribute("username", oidcUserService.getName());
        }
        return "Home/index";
    }
}

