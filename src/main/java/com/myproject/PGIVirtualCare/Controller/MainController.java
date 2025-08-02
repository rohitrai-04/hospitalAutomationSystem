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
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EnquiryRepository enquiryrepo;

    @Autowired
    private HttpSession session;

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

   @GetMapping("/adminLogin")
public String ShowAdminLogin() {
    return "adminLogin";
}

@PostMapping("/adminLogin")
public String AdminLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
    try {
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();

        // System.out.println("Login attempt for email: " + email);

        if (!userRepo.existsByEmail(email)) {
            attributes.addFlashAttribute("msg", "User Doesn't Exist");
            return "redirect:/adminLogin"; 
        }
        
        Users admin = userRepo.findByEmail(email);
        // System.out.println("Fetched admin: " + admin);
        // System.out.println("Stored password: '" + admin.getPassword() + "'");
        // System.out.println("Input password: '" + password + "'");
        // System.out.println("Admin role: " + admin.getRole());

        if (password.equals(admin.getPassword()) && admin.getRole().equals(userRole.ADMIN)) {
            attributes.addFlashAttribute("msg", "Welcome to Admin Dashboard");
            session.setAttribute("loggedInAdmin",admin);
            return "redirect:/Admin/adminDashboard";
            // TODO: Redirect to actual admin dashboard page instead of login page
        } else {
            attributes.addFlashAttribute("msg", "Invalid Id / Password");
        }
        return "redirect:/adminLogin";  
    } catch (Exception e) {
        attributes.addFlashAttribute("msg", "Error :" + e.getMessage());
        return "redirect:/adminLogin";
    }
}

}
