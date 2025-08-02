package com.myproject.PGIVirtualCare.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.PGIVirtualCare.Model.Users;
import com.myproject.PGIVirtualCare.Model.Users.userRole;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Users findByEmail(String email);

	List<Users> findAllByRole(userRole patient);

}
