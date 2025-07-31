package com.myproject.PGIVirtualCare.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIVirtualCare.Model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

}
