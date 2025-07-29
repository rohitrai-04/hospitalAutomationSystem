package com.myproject.PGIVirtualCare.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Model.Users.userRole;
import com.myproject.PGIVirtualCare.Model.Users.userStatus;
import com.myproject.PGIVirtualCare.Repository.UserRepository;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String ShowIndex() {
        return "index";
    }

    @GetMapping("/registration")
    public String ShowRegistration(Model model) {
        Users userDto = new Users();
        model.addAttribute("userDto", userDto);

        return "registration";
    }

    @PostMapping("/registration")
    public String Registration(@ModelAttribute("userDto") Users newUser, RedirectAttributes attributes) {
        try {
            newUser.setRole(userRole.PATIENT);
            newUser.setStatus(userStatus.PENDING);
            newUser.setDate(LocalDateTime.now());
            userRepo.save(newUser);
            attributes.addFlashAttribute("msg", "Registration Successful ");
            return "redirect:/registration";

        } catch (Exception e) {
            attributes.addFlashAttribute("msg", "Error :"+e.getMessage());
            return "redirect:/registration";
        } 
    }
}
