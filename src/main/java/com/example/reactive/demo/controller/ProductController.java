package com.example.reactive.demo.controller;

import com.example.reactive.demo.dto.ProductDto;
import com.example.reactive.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/find/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id) {
        return productService.getProductById(Integer.parseInt(id));
    }

    @GetMapping("/range")
    public Flux<ProductDto> getProductsBetweenPriceRange(@RequestParam("min") double min, @RequestParam("max") double max) {
        return productService.getProductsByPriceRange(min, max);
    }

    @PostMapping("/save")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return productService.saveProduct(productDtoMono);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(Integer.parseInt(id));
    }
}
