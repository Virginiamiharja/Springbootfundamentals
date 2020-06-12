package com.cimb.tokolapak.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.tokolapak.entity.EmployeeAddress;

// Ada 2 parameter yang pertama tipe data yaitu object si EmployeeAddress yg kedua tipe id nya
public interface EmployeeAddressRepo extends JpaRepository<EmployeeAddress, Integer> {

}
