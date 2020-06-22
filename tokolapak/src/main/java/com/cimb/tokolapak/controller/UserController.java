package com.cimb.tokolapak.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private EmailUtil emailUtil;
	
	@PostMapping
	public User registerUser(@RequestBody User user) {
//		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
//		
//		if(findUser.toString() != "Optional.empty") {
//			throw new RuntimeException("Username exists!");
//		}
		
		String encodedPassword = pwEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
		
		User savedUser = userRepo.save(user);
		
		savedUser.setPassword(null);
		
//		this.emailUtil.sendEmail("virgnm@gmail.com", "User Registration", "Welcome to Our Club");
		this.emailUtil.sendEmail("virgnm@gmail.com", "Tes ya", "Dari virgi");
	
		return savedUser;
	}
	
	// Login pake method post
	@PostMapping("/login")
	public User loginUser (@RequestBody User user) {
		User findUser = userRepo.findByUsername(user.getUsername()).get();
							// Password raw		Password sudah encode
		if(pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		}
		
		throw new RuntimeException("Wrong password");
//		return null;
	}
	
	// Jadi nanti nulisnya begitu
	// Login pake method get pake request params kalo di axios begini localhost:8080/login?username=seto&password=pass123
	@GetMapping("/login")
	public User getLoginUser (@RequestParam String username, @RequestParam String password) {
		User findUser = userRepo.findByUsername(username).get();
		
							// Password raw		Password sudah encode
		if(pwEncoder.matches(password, findUser.getPassword())) {
			findUser.setPassword(null);
			return findUser;
		}
		
		return null;
	}
	
	@GetMapping
	public Iterable<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@PostMapping("/sendemail")
	public String sendEmailTesting() {
		this.emailUtil.sendEmail("virgnm@gmail.com", "Tes ya", "Dari virgi");
		return "Email sent!";
	}
	
	
	
	

}
