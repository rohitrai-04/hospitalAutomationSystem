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
import com.myproject.PGIVirtualCare.Model.Prescription;
import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Repository.AppointmentRepository;
import com.myproject.PGIVirtualCare.Repository.PrescriptionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Doctor")
public class DoctorController {

    @Autowired
    private HttpSession session;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionRepository prescriptionReposit;

    @GetMapping("/doctorDashboard")
    public String ShowDoctorDashboard() {
        if (session.getAttribute("loggedInDoctor") == null) {
            return "redirect:/DoctorLogin";
        }
        return "Doctor/doctorDashboard";
    }

    @GetMapping("/ViewAppointments")
    public String ViewAppointment(Model model) {
        if (session.getAttribute("loggedInDoctor") == null) {
            return "redirect:/DoctorLogin";
        }

        Users doctor = (Users) session.getAttribute("loggedInDoctor");
        List<Appointment> appointment = appointmentRepo.findAll();
        model.addAttribute("appointment", appointment);
        return "Doctor/ViewAppointment";
    }

    @GetMapping("/Appointments")
    public String Appointment(Model model) {
        if (session.getAttribute("loggedInDoctor") == null) {
            return "redirect:/DoctorLogin";
        }

        Users doctor = (Users) session.getAttribute("loggedInDoctor");
        List<Appointment> appList = appointmentRepo.findAllByDoctorName(doctor);
        model.addAttribute("appList", appList);
        return "Doctor/Appointment";
    }

    @GetMapping("/writeprescription")
    public String ShowPrescription(@RequestParam("id") long id, Model model) {
        if (session.getAttribute("loggedInDoctor") == null) {
            return "redirect:/DoctorLogin";
        }
        Appointment appointment = appointmentRepo.findById(id).get();
        model.addAttribute("appointment", appointment);
        Prescription prescription = new Prescription();
        model.addAttribute("prescription", prescription);
        return "Doctor/writeprescription";
    }

    @PostMapping("/writeprescription")
    public String writePrescription(@ModelAttribute Prescription prescription,
            RedirectAttributes attributes,
            @RequestParam("appointmentId") long appointmentId) {
        try {
            Appointment appointment = appointmentRepo.findById(appointmentId).get();
            prescription.setAppointment(appointment); // fixed setter

            prescription.setPrescriptionDAte(LocalDateTime.now());
            prescriptionReposit.save(prescription);

            attributes.addFlashAttribute("msg", "Prescription successfully submitted");
            return "redirect:/Doctor/Appointments";
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log the error for debugging
            return "redirect:/Doctor/writeprescription?id=" + appointmentId; // fallback redirect
        }
    }

}
