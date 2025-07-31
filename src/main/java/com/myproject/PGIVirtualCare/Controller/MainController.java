package com.myproject.PGIVirtualCare.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIVirtualCare.Model.Enquiry;
import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Model.Users.userRole;
import com.myproject.PGIVirtualCare.Model.Users.userStatus;
import com.myproject.PGIVirtualCare.Repository.EnquiryRepository;
import com.myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {
     
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EnquiryRepository enquiryrepo;

    @GetMapping("/")
    public String ShowIndex() {
        return "index";
    }

    @GetMapping("/about")
    public String ShowAbout() {
        return "about";
    }

    @GetMapping("/services")
    public String ShowService() {
        return "service";
    }

    @GetMapping("/contact")
    public String ShowContact(Model model) {
        Enquiry enquiry = new Enquiry();
        model.addAttribute("enquiry", enquiry);
        return "contact";
    }

    @GetMapping("/login")
    public String ShowPatientLogin() {
        return "patientLogin";
    }

    @PostMapping("/login")
    public String PatientLogin(HttpServletRequest request, RedirectAttributes attributes) {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (!userRepo.existsByEmail(email)) {
                attributes.addFlashAttribute("msg", "User Does't Exist");
                return "redirect:/login";
            }

            Users patient = userRepo.findByEmail(email);

            if (password.equals(patient.getPassword()) && patient.getRole().equals(userRole.PATIENT)) {
                if (patient.getStatus().equals(userStatus.PENDING)) {
                    attributes.addFlashAttribute("msg", "Registration Pending , Please wait for Admin Approval");

                } else if (patient.getStatus().equals(userStatus.DISABLED)) {
                    attributes.addFlashAttribute("msg", "Login Disabled ,Please Contact Administration");

                } else {
                    attributes.addFlashAttribute("msg", "Valid user");

                }
            } else {
                attributes.addFlashAttribute("msg", "Invalid User / Password");
            }
            return "redirect:/login";
        } catch (Exception e) {
            return "redirect:/login";
        }
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
            attributes.addFlashAttribute("msg", "Error :" + e.getMessage());
            return "redirect:/registration";
        }
    }

    @PostMapping("/contact")
    public String Contact(@ModelAttribute("enquiry") Enquiry newEnquiry, RedirectAttributes attributes) {
        try {
            newEnquiry.setEnquiryDate(LocalDateTime.now());
            enquiryrepo.save(newEnquiry);
            attributes.addFlashAttribute("enqMsg", "Enquiry sent successfully!");
        } catch (Exception e) {
            attributes.addFlashAttribute("enqMsg", "Error: " + e.getMessage());
        }
        return "redirect:/contact";
    }

}
