package com.cimb.tokolapak.service;
import java.util.Optional;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.EmployeeAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeService {
	
	public void deleteEmployeeAddress(EmployeeAddress employeeAddress);

}




