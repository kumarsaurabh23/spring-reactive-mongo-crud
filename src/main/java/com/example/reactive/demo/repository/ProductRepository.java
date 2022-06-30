package com.example.reactive.demo.repository;

import com.example.reactive.demo.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, Integer> {
    Flux<Product> findByPriceBetween(Range<Double> price);
}
