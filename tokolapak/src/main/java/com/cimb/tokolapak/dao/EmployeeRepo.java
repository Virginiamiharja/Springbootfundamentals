package com.cimb.tokolapak.dao;
import com.cimb.tokolapak.entity.Department;
import com.cimb.tokolapak.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
	
	public Iterable<Employee> findByDepartment(Department department);

}
