package com.cimb.tokolapak.controller;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.EmployeeAddressRepo;
import com.cimb.tokolapak.dao.EmployeeRepo;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.EmployeeAddress;
import com.cimb.tokolapak.entity.Product;
import com.cimb.tokolapak.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeAddressRepo employeeAddressRepo;
	
	@PostMapping
	public Employee addEmployee(@RequestBody Employee employee) {
		return employeeRepo.save(employee);
	}
	
	@GetMapping
	// Iterable itu kaya arraynya 
	public Iterable<Employee> getEmployees() {
		return employeeRepo.findAll();
	}
	
	@DeleteMapping("/address/{id}")
	public void deleteEmployeeAddressById(@PathVariable int id) {
//		Ini untuk ngecek ada ga employee address dengan id ini
		Optional<EmployeeAddress> employeeAddress = employeeAddressRepo.findById(id);
		
		if(employeeAddress.get() == null) {
			throw new RuntimeException("Employee address not found");
		} 
		
		employeeService.deleteEmployeeAddress(employeeAddress.get());
	}
	
	@PutMapping
	public Employee addAddressById(@RequestBody Employee employee) {
		Optional <Employee> findEmployee = employeeRepo.findById(employee.getId());
		
		if(findEmployee.toString() == "Optional.empty")
			throw new RuntimeException("Employee with id " + employee.getId() + " does not exist");
		
		return employeeRepo.save(employee);
	}

	
//	WEEKEND TASK - Ini add address berdasarkan employee ID
	@PostMapping("/{id}")
	public Optional<Employee> addEmployeeAddressById(@PathVariable int id, @RequestBody EmployeeAddress employeeAddress) {
	
		Optional<Employee> employee = employeeRepo.findById(id);
		
//		Sebelum di save address ambil data dulu employee yang mana
		employeeAddress.setEmployee(employee.get());
		employeeAddressRepo.save(employeeAddress);
//		Setelah di save baru si employee yang ambil data address
		employeeAddress.getEmployee().setEmployeeAddress(employeeAddress);
//		Trs di save, karena kalo ga disave ya ora iso Virginia 
		employeeRepo.save(employee.get());

		return employeeRepo.findById(id);
	}
	
}
