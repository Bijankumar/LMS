package com.te.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.te.lms.entity.EmployeePrimaryInfo;

@Repository
public interface LmsEmployeeRepository extends JpaRepository<EmployeePrimaryInfo, Integer> {

	EmployeePrimaryInfo findByemployeeEmail(String username);
}
