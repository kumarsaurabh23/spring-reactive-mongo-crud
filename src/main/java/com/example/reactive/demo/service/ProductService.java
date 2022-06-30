package com.example.reactive.demo.service;

import com.example.reactive.demo.dto.ProductDto;
import com.example.reactive.demo.entity.Product;
import com.example.reactive.demo.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private ModelMapper mapper = new ModelMapper();

    public Flux<ProductDto> getAllProducts() {
       return productRepository.findAll()
               .map(p -> mapper.map(p, ProductDto.class));
    }

    public Mono<ProductDto> getProductById(int id) {
        return productRepository.findById(id)
                .map(p -> mapper.map(p, ProductDto.class));
    }

    public Flux<ProductDto> getProductsByPriceRange(double min, double max) {
        return productRepository.findByPriceBetween(Range.closed(min, max))
                .map(p -> mapper.map(p, ProductDto.class));
    }

    public Mono<Void> deleteProduct(int id) {
        productRepository.deleteById(id);
        return Mono.empty();
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productMono) {
        return productMono.map(p -> mapper.map(p, Product.class))
                .flatMap(productRepository::save)
                .map(p -> mapper.map(p, ProductDto.class));
    }
}
