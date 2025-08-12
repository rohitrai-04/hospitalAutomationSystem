package com.myproject.PGIVirtualCare.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Prescription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMedicationDetails() {
		return medicationDetails;
	}

	public void setMedicationDetails(String medicationDetails) {
		this.medicationDetails = medicationDetails;
	}

	public String getAdvice() {
		return Advice;
	}

	public void setAdvice(String advice) {
		Advice = advice;
	}

	public String getDiagonsis() {
		return diagonsis;
	}

	public void setDiagonsis(String diagonsis) {
		this.diagonsis = diagonsis;
	}

	public LocalDateTime getPrescriptionDAte() {
		return prescriptionDAte;
	}

	public void setPrescriptionDAte(LocalDateTime prescriptionDAte) {
		this.prescriptionDAte = prescriptionDAte;
	}

	@Column(nullable = false)
	private String medicationDetails;

	@Column(nullable = false, length = 500)
	private String Advice;

	@Column(nullable = false, length = 500)
	private String diagonsis;
	private LocalDateTime prescriptionDAte;

	@ManyToOne
	private Appointment appointment;
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

}
