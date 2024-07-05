package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import JAVAC5.com.WebOrderDoAn.Entities.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/Home")
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String home(@AuthenticationPrincipal User user, @AuthenticationPrincipal OAuth2User oidcUserService, Model model) {
        if(user != null) {
            model.addAttribute("username", user.getUsername());
        }
        else if(oidcUserService != null) {
            model.addAttribute("username", oidcUserService.getName());
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Home/index";
    }
}

