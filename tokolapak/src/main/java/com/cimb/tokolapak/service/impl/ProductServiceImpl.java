package com.cimb.tokolapak.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cimb.tokolapak.dao.ProductRepo;
import com.cimb.tokolapak.entity.Product;
import com.cimb.tokolapak.service.ProductService;

// Ini untuk menandakan kelas ini sebuah service
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private	ProductRepo productRepo;

	@Override
	// Transactional ini menghindari ada sequence query ketika ada salah satu yang gagal, semuanya langsung diulang
	@Transactional
	public Iterable<Product> getProducts() {
		return productRepo.findAll();
	}

	@Override
	@Transactional
	public Optional<Product> getProductById(int id) {
		return productRepo.findById(id);
	}

	@Override
	public Product addProduct(Product product) {
//		Post sama update itu intinya sama, sama2 ngesave
//		Jadi disini kita make sure aja kita ngirim id 0 dulu, nanti auto incrementnya baru deh urusan database
		product.setId(0);
		return productRepo.save(product);
	}

	@Override
	public Product updateProduct(Product product) {
		Optional <Product> findProduct = productRepo.findById(product.getId());
		
		if(findProduct.toString() == "Optional.empty")
			throw new RuntimeException("Product with id " + product.getId() + " does not exist");
		
		return productRepo.save(product);
	}

	@Override
	public void deleteProductById(int id) {
		Optional <Product> findProduct = productRepo.findById(id);
		
		if(findProduct.toString() == "Optional.empty")
			throw new RuntimeException("Product with id " + id + " does not exist");
		
		productRepo.deleteById(id);
	}

}
