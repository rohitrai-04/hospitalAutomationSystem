package com.myproject.PGIVirtualCare.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table

public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;

	private String contactNumber;

	@Column(unique = true,nullable = false)
	private String email;
	private int age;

	@Column(length = 500)
	private String address;

	@Column(nullable = false, length = 12)
	private String password;
	private String gender;
	private String aadharNumber;
	private String specialization;
	private LocalDateTime date;

	@Column(length = 1000)
	private String profilePic;
	private String avaliableTime;

	@Enumerated(EnumType.STRING)
	private userStatus status;

	@Enumerated(EnumType.STRING)
	private userRole role;

	public enum userStatus {
		PENDING, APPROVED, DISABLED
	}

	public enum userRole {
		ADMIN, DOCTOR, PATIENT,
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getAvaliableTime() {
		return avaliableTime;
	}

	public void setAvaliableTime(String avaliableTime) {
		this.avaliableTime = avaliableTime;
	}

	public userStatus getStatus() {
		return status;
	}

	public void setStatus(userStatus status) {
		this.status = status;
	}

	public userRole getRole() {
		return role;
	}

	public void setRole(userRole role) {
		this.role = role;
	}
}
        