package com.cimb.tokolapak.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Rest controller ini untuk menandakan si HelloWorldController itu adalah sebuah controller
@RestController
//Kasih prefix utk route ini fungsinya biar ceritanya untuk masuk admin gitu deh, 
//Jd si hello bisa diakses ketika /admin/hello
@RequestMapping("/admin")
public class HelloWorldController {
	//Get mapping adalah ketika ada sebuah get-request dr front-end dia akan masuk ke method helloWorld
	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello namaku Virginia";	
	}
	
	@GetMapping("/hello/{name}")
	//Path variable untuk dapetin parameter atau yg didalem kurung kurawalnya getMapping, trs parameter di route dan di func harus sama
	public String helloName(@PathVariable() String name) {
		return "Hello " + name;
	}

	@GetMapping("/angka/{nomor}")
	public int angka(@PathVariable() int nomor) {
		return nomor;
	}
}
