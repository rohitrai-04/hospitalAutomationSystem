package com.myproject.PGIVirtualCare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIVirtualCare.Model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{

}
