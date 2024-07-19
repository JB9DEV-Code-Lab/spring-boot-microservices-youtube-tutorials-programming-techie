package codelab.jb9dev.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import codelab.jb9dev.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
