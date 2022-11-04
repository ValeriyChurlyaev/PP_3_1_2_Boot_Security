package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String getAllUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }


    @GetMapping("/new")
    public String addUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/saveUser")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "select_role", required = false) String[] roles) {
        userService.saveUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String update(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("role", roleService.getAllRoles());
        return "update";
    }

    @PostMapping(value = "/update/{id}")
    public String update(long id, User user,
                         @RequestParam(value = "select_role", required = false) String[] roles) {
        userService.updateUser(id, user, roles);
        return "redirect:/admin";
    }


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
