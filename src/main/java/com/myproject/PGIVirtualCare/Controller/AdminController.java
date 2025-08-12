package com.myproject.PGIVirtualCare.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIVirtualCare.Model.Appointment;
import com.myproject.PGIVirtualCare.Model.Enquiry;
import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Model.Users.userRole;
import com.myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.myproject.PGIVirtualCare.Repository.EnquiryRepository;
import com.myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
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
	
	@Autowired
	private AppointmentRepository appointmentRepo;


	@GetMapping("/adminDashboard")
	public String ShowAdminDashboard(Model model) {
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/adminLogin";
	    }

	    // User stats
	    long totalDoctors = userRepo.countByRole(Users.userRole.DOCTOR);
	    long totalPatients = userRepo.countByRole(Users.userRole.PATIENT);
	    long approvedPatients = userRepo.countByRoleAndStatus(Users.userRole.PATIENT, Users.userStatus.APPROVED);
	    long rejectedPatients = userRepo.countByRoleAndStatus(Users.userRole.PATIENT, Users.userStatus.DISABLED);

	    // Appointment stats
	    long totalAppointments = appointmentRepo.count();
	    long approvedAppointments = appointmentRepo.countByStatus(Appointment.AppointmentStatus.APPROVED);
	    long rejectedAppointments = appointmentRepo.countByStatus(Appointment.AppointmentStatus.REJECTED);
	    long pendingAppointments = appointmentRepo.countByStatus(Appointment.AppointmentStatus.PENDING);
	    long rescheduledAppointments = appointmentRepo.countByStatus(Appointment.AppointmentStatus.RESHEDULED);

	    // Add to model
	    model.addAttribute("totalDoctors", totalDoctors);
	    model.addAttribute("totalPatients", totalPatients);
	    model.addAttribute("approvedPatients", approvedPatients);
	    model.addAttribute("rejectedPatients", rejectedPatients);
	    model.addAttribute("totalAppointments", totalAppointments);
	    model.addAttribute("approvedAppointments", approvedAppointments);
	    model.addAttribute("rejectedAppointments", rejectedAppointments);
	    model.addAttribute("pendingAppointments", pendingAppointments);
	    model.addAttribute("rescheduledAppointments", rescheduledAppointments);

	    return "Admin/adminDashboard";
	}


	@GetMapping("/ManagePatients")
	public String ShowManagePatient(Model model) {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}
		List<Users> patientsList = userRepo.findAllByRole(userRole.PATIENT);
		model.addAttribute("patientsList", patientsList.reversed());

		return "Admin/managePatient";
	}

	@GetMapping("/DeletePatient")
	public String DeletePatient(@RequestParam("id") long id) {
		userRepo.deleteById(id);
		return "redirect:/Admin/ManagePatients";
	}

	@GetMapping("/Enquiry")
	public String ShowEnquiry(Model model) {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}
		List<Enquiry> enquiryList = enquiryRepo.findAll();
		model.addAttribute("enquiryList", enquiryList);
		return "Admin/Enquiries";

	}

	@GetMapping("/AddDoctors")
	public String ShowAddDoctors(Model model) {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}
		Users doctor = new Users();
		model.addAttribute("doctor", doctor);

		return "Admin/AddDoctors";

	}

	@PostMapping("/AddDoctors")
	public String AddDoctor(@ModelAttribute("doctor") Users doctor, RedirectAttributes attributes) {
		try {
			if (userRepo.existsByEmail(doctor.getEmail())) {
				attributes.addFlashAttribute("msg", "User Already Exist with this email");
				return "redirect:/Admin/AddDoctors";
			}
			doctor.setRole(userRole.DOCTOR);
			doctor.setStatus(Users.userStatus.APPROVED);
			doctor.setDate(LocalDateTime.now());
			userRepo.save(doctor);
			attributes.addFlashAttribute("msg", "Doctor Successfully Added");
			return "redirect:/Admin/AddDoctors";
		} catch (Exception e) {
			// TODO: handle exception
			return "redirect:/Admin/AddDoctors";
		}
	}

	@GetMapping("/Logout")
	public String Logout(RedirectAttributes attributes) {
		session.removeAttribute("loggedInAdmin");
		attributes.addFlashAttribute("msg", "Logout Successfully");
		return "redirect:/adminLogin";
	}

	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}
		return "Admin/ChangePassword";
	}

	@PostMapping("ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confPass = request.getParameter("confirmPassword");

			if (!newPass.equals(confPass)) {
				attributes.addFlashAttribute("msg", "New Password and Old Password are not same");
				return "redirect:/Admin/ChangePassword";
			}

			Users admin = (Users) session.getAttribute("loggedInAdmin");

			if (oldPass.equals(admin.getPassword())) {
				admin.setPassword(confPass);
				userRepo.save(admin);
				session.removeAttribute("loggedInAdmin");
				attributes.addFlashAttribute("msg", "Password successfully changed");
				return "redirect:/adminLogin";
			} else {
				attributes.addFlashAttribute("msg", "Invalid Old Password");

			}
			return "redirect:/Admin/ChangePassword";
		} catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("msg", "Error: +e.getMessage()");
			return "redirect:/Admin/ChangePassword";
		}

	}

	@GetMapping("/ApprovePatient")
	public String approvePatient(@RequestParam("id") Long id, RedirectAttributes attributes) {
		Users patient = userRepo.findById(id).orElse(null);
		if (patient != null && patient.getRole() == userRole.PATIENT) {
			patient.setStatus(Users.userStatus.APPROVED);
			userRepo.save(patient);
			attributes.addFlashAttribute("msg", "Patient approved successfully.");
		} else {
			attributes.addFlashAttribute("msg", "Patient not found or invalid.");
		}
		return "redirect:/Admin/ManagePatients";
	}

	@GetMapping("/RejectPatient")
	public String rejectPatient(@RequestParam("id") Long id, RedirectAttributes attributes) {
		Users patient = userRepo.findById(id).orElse(null);
		if (patient != null && patient.getRole() == userRole.PATIENT) {
			patient.setStatus(Users.userStatus.DISABLED);
			userRepo.save(patient);
			attributes.addFlashAttribute("msg", "Patient rejected successfully.");
		} else {
			attributes.addFlashAttribute("msg", "Patient not found or invalid.");
		}
		return "redirect:/Admin/ManagePatients";
	}

}
