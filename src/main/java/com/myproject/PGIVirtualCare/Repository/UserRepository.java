package com.myproject.PGIVirtualCare.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIVirtualCare.Model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Users findByEmail(String email);

}
