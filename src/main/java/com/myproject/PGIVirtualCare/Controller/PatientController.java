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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.PGIVirtualCare.Model.Appointment;
import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.myproject.PGIVirtualCare.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Patient")
public class PatientController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/patientDashboard")
    public String PatientDashboard() {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login";
        }
        return "Patient/patientDashboard";
    }

    @GetMapping("/BookAppointment")
    public String ShowBookedAppointment(Model model) {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login";
        }

        List<Users> doctorList = userRepo.findAllByRole(Users.userRole.DOCTOR);
        model.addAttribute("doctorList", doctorList);
        Appointment appointment = new Appointment();
        model.addAttribute("appointment", appointment);
        return "Patient/BookedAppointment";
    }

    @PostMapping("/BookAppointment")
    public String BookedAppointment(@ModelAttribute("appointment") Appointment appointment, RedirectAttributes attributes) {
        try {
            Users patient = (Users) session.getAttribute("loggedInPatient");

            appointment.setPatientName(patient);
            appointment.setDepartment(appointment.getDoctorName().getSpecialization());
            appointment.setStatus(Appointment.AppointmentStatus.PENDING);
            appointment.setBookedAt(LocalDateTime.now());
            appointmentRepo.save(appointment);
            attributes.addFlashAttribute("msg", "Appointment was successfully placed");
            return "redirect:/Patient/patientDashboard";
        } catch (Exception e) {
            // TODO: handle exception
            attributes.addFlashAttribute("msg", e.getMessage());

            return "redirect:/Patient/patientDashboard";
        }
    }

    @GetMapping("/viewAppointment")
    public String ViewAppointment(Model model) {
        if (session.getAttribute("loggedInPatient") == null) {
            return "redirect:/login";
        }
     
        Users patient = (Users) session.getAttribute("loggedInPatient");
        List<Appointment> appointment = appointmentRepo.findAllByPatientName(patient);
        model.addAttribute("appointment", appointment);
        return "Patient/viewAppointment";
    }
}
