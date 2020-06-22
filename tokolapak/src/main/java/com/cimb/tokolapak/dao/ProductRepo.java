package com.cimb.tokolapak.dao;
import com.cimb.tokolapak.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//Ini kelas yang berhubungan langsung dengan si database, nah karena dia itu extends CrudRepository makanya dia ada ada functions
public interface ProductRepo extends CrudRepository<Product, Integer> {
	
	// Kalo method ini udh langsung dibikinin querynya sama JPA
	public Product findByProductName(String productName);
	
	// Indexed parameters
	// Kalo ini kita query sendiri
	// Ini dikasih native query karena biar dia pake query bawaan mySQL bukan query dr JPQL
	@Query(value = "SELECT * From Product where Price > ?1 and Product_Name = ?2", nativeQuery = true)
	public Iterable <Product> findProductByMinPrice(double minPrice, String productName);
	
	// Named parameters
	// Alternatif kedua
	@Query(value = "SELECT * From Product where Price < :maxPrice and Product_Name like %:productName%", nativeQuery = true)
	public Iterable <Product> findProductByMaxPrice(@Param("maxPrice") double maxPrice, @Param("productName") String namaProduk);	
}
