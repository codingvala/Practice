package com.example.springannotationdemo.web;

import com.example.springannotationdemo.component.GreetingComponent;
import com.example.springannotationdemo.model.User;
import com.example.springannotationdemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DemoController {
    private final GreetingComponent greetingComponent;
    private final UserService userService;

    public DemoController(GreetingComponent greetingComponent, UserService userService) {
        this.greetingComponent = greetingComponent;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("greeting", greetingComponent.getGreeting());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userForm", new User());
        return "index";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute("userForm") User user) {
        userService.save(user);
        return "redirect:/";
    }
}
