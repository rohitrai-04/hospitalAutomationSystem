package com.myproject.PGIVirtualCare.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.myproject.PGIVirtualCare.Model.Enquiry;
import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Model.Users.userRole;
import com.myproject.PGIVirtualCare.Repository.EnquiryRepository;
import com.myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EnquiryRepository enquiryRepo;
    
    
    @GetMapping("/adminDashboard")
    public String ShowAdminDashboard() {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/adminLogin";
        }
        return "Admin/adminDashboard";
    }

    @GetMapping("/ManagePatients")
    public String ShowManagePatient(Model model) {
        if(session.getAttribute("loggedInAdmin") == null){
            return "redirect:/adminLogin";
        }
        List<Users> patientsList = userRepo.findAllByRole(userRole.PATIENT);
        model.addAttribute("patientsList", patientsList);
        
        return "Admin/managePatient";
    }

    @GetMapping("/Enquiry")
    public String ShowEnquiry(Model model){
        if(session.getAttribute("loggedInAdmin") ==null){
            return "redirect:/adminLogin";
        }
        List<Enquiry> enquiryList = enquiryRepo.findAll();
        model.addAttribute("enquiryList", enquiryList);
        return "Admin/Enquiries";

    }
}
