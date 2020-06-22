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
import com.cimb.tokolapak.dao.DepartmentRepo;
import com.cimb.tokolapak.dao.EmployeeAddressRepo;
import com.cimb.tokolapak.dao.EmployeeRepo;
import com.cimb.tokolapak.dao.ProjectRepo;
import com.cimb.tokolapak.entity.Department;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.EmployeeAddress;
import com.cimb.tokolapak.entity.Product;
import com.cimb.tokolapak.entity.Project;
import com.cimb.tokolapak.service.DepartmentService;
import com.cimb.tokolapak.service.EmployeeService;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeAddressRepo employeeAddressRepo;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired 
	private DepartmentRepo departmentRepo;
	
	@Autowired
	private ProjectRepo projectRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@PostMapping
	public Employee addEmployee(@RequestBody Employee employee) {
		return employeeRepo.save(employee);
	}
	
	@PostMapping("/department/{departmentId}")
	public Employee addEmployeewithDepartment(@PathVariable int departmentId, @RequestBody Employee employee) {
		Department department = departmentRepo.findById(departmentId).get();
		 
		if(department == null)
			throw new RuntimeException("Department not found");
		
		employee.setDepartment(department);
		emailUtil.sendEmail(employee.getEmail(), "REGISTRASI KARYAWAN", "<h1>SELAMAT</h1> Anda telah bergabung bersama kami");
		
		return employeeRepo.save(employee);
	}
	
	@PostMapping("{employeeId}/projects/{projectId}")
	public Employee addProjectToEmployee(@PathVariable int employeeId, @PathVariable int projectId) {
		Employee findEmployee = employeeRepo.findById(employeeId).get();
		
		Project findProject = projectRepo.findById(projectId).get();
		
//		Get project itu kan sebuah list, makanya dia bisa ngeadd
		findEmployee.getProjects().add(findProject);
		
		return employeeRepo.save(findEmployee);
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
	
	// Cara nambahin employee address dari kakaknya
	@PutMapping("/{employeeId}/address")
	public Employee addAddressToEmployee(@PathVariable int id, @RequestBody EmployeeAddress employeeAddress) {
		Employee findEmployee = employeeRepo.findById(id).get();
		
		if(findEmployee == null) throw new RuntimeException("Employee not found");
		
		findEmployee.setEmployeeAddress(employeeAddress);
		
		return employeeRepo.save(findEmployee);
	}
	
	@PutMapping("{employeeId}/department/{departmentId}")
	public Employee addEmployeeToDepartment(@PathVariable int departmentId, @PathVariable int employeeId) {
		Employee findEmployee = employeeRepo.findById(employeeId).get();
		
		if(findEmployee == null)
			throw new RuntimeException("Employee not found");
		
		Department department = departmentRepo.findById(departmentId).get();
		if(department == null)
			throw new RuntimeException("Department not found");
		
		findEmployee.setDepartment(department);
		return employeeRepo.save(findEmployee);
		
//		Harusnya si pasti yang masuk cuma 1 tapi pake map untuk mempermudah aja katanya
//		return departmentRepo.findById(departmentId).map(department -> {
//			findEmployee.setDepartment(department);
//			return employeeRepo.save(findEmployee);
//		}).orElseThrow(() -> new RuntimeException("Department not found"));	
//	}
	}
}
