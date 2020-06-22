package com.cimb.tokolapak.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/documents")
@CrossOrigin
public class DocumentController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	// Buat kasih tau ke java kalo misalnya ada upload file upload kesini loh				
	private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
	
	@GetMapping("/testing")
	public void testing() {
		System.out.println(uploadPath);
	}
	
	@GetMapping("/verified/{email}")
	public String verifiedUser (@PathVariable String email) {
		User findUser = userRepo.findByEmail(email).get();
//		
//		if(findUser == null)
//			throw new RuntimeException("User not found");
		
		findUser.setVerified(true);
		userRepo.save(findUser);
		return "Sukses";
	}
	
	@PostMapping
	// Request param disini itu bukan minta route param gitu tapi dia minta form data
	// Ini menandakan yang ada di key "file" adalah sebuah multipartfile dan disimpan dalam variable file
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam ("userData") String userString) throws JsonMappingException, JsonProcessingException {
		Date date = new Date();					// Ini bisa dari tipe data apa aja jadi tipe data apa aja
												// Ini user dalam bentuk string mau diubah ke dalam bentuk object kaya User.class nah ini asalnya dr entity user, trs nanti errornya di hover trs klik add throws declaration
		User user = new ObjectMapper().readValue(userString, User.class);
		
		System.out.println("USERNAME: " + user.getUsername());
		
		//Tugas.pdf buat ambil extensionnya aja jadi pdf
		String fileExtension = file.getContentType().split("/")[1];
		System.out.println(fileExtension);
		String newFileName = "PROD-" + user.getId() + "." + fileExtension;
		
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		String fileName = StringUtils.cleanPath(newFileName);
		
		// Ini untuk kasih tau ke java, tujuannya kesini dan nama filenyaini ya 
		// String utils clean path itu kaya buat rapihin pathnya dari ya \\x\\yy jadi x/y gitu 
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		
		try {
			// Ini kit suruh copy, trs param 1 file yang mau diambil, param 2 tujuannya kemana yaitu si path, 
			// Trs param 3 config dr method copy, jadi replace existing kalo misalnya udh ada file dengan nama yang sama dia akan nimpa
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		return fileName + " has been uploaded";
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documents/download/").path(fileName).toUriString();
		user.setProfilePicture(fileDownloadUri);
		userRepo.save(user);
		
		// Ini kenapa dia gabisa di klik coba linknya huft
//		<html><body><a href="localhost:8080/documents/verified/"+>Verification Link<>

		String link = "localhost:8080/documents/verified/" + user.getEmail();
		
		this.emailUtil.sendEmail(user.getEmail(), "User Registration", "Hi " + user.getUsername() + " please kindly paste the link to verified your account " + link );
		return fileDownloadUri;
	}
	
//	Buat buka file 
//	Axios.post(API.documents, file).then( res => windows.open(res.data))
// 	Dan nanti pas di save di database, di save urlnya
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName) {
		Path path = Paths.get(uploadPath + fileName);
		Resource resource = null;
		
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		System.out.println("DOWNLOAD");
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + resource.getFilename() + "\"").body(resource);
	}
	
	@PostMapping("/login")
	public User loginWithProfilePicture(@RequestBody User user) {
		return userRepo.findByUsername(user.getUsername()).get();
	}

	
	
// Jangan lupa di application properties tambahin 2 line ini, soalnya kalo ga dia error karena max size itu 1mb
	//spring.servlet.multipart.max-file-size=10MB
	//spring.servlet.multipart.max-request-size=15MB

}
