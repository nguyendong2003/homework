package com.example.homework.controller;

import com.example.homework.model.User;
import com.example.homework.repository.UserRepository;
import com.example.homework.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult result, Model model) {
        if (userService.existsByUsername(user.getUsername())) { // Kiểm tra username đã tồn tại chưa
            model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            return "auth/register"; // Quay lại trang đăng ký với thông báo lỗi
        }
        user.setRoles(Collections.singleton("USER")); // Mặc định là USER
        userService.saveUser(user); // Nếu chưa tồn tại thì lưu vào database
        return "redirect:/login?success"; // Chuyển hướng đến trang đăng nhập nếu thành công
    }
}
