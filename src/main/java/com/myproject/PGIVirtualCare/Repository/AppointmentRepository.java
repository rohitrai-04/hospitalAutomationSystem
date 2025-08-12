package com.myproject.PGIVirtualCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIVirtualCare.Model.Appointment;
import com.myproject.PGIVirtualCare.Model.Appointment.AppointmentStatus;
import com.myproject.PGIVirtualCare.Model.Users;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	

	List<Appointment> findAllByPatientName(Users patient);



	List<Appointment> findAllByDoctorName(Users doctor);



	long countByStatus(AppointmentStatus approved);



}
