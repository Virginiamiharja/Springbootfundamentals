package com.cimb.tokolapak.dao;
import com.cimb.tokolapak.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

//Ini kelas yang berhubungan langsung dengan si database, nah karena dia itu extends CrudRepository makanya dia ada ada functions
public interface ProductRepo extends CrudRepository<Product, Integer> {
	public Product findByProductName(String productName);
}
