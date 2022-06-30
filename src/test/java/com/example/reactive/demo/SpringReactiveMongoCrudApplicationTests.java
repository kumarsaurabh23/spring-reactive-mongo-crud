package com.example.reactive.demo;

import com.example.reactive.demo.controller.ProductController;
import com.example.reactive.demo.dto.ProductDto;
import com.example.reactive.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	@Test
	void testSaveProduct() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(1, "mobile", 599, 2));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products/save")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange().expectStatus().isOk();
	}

	@Test
	void testGetAllProducts() {
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto(1, "mobile", 599, 2),
				new ProductDto(2, "laptop", 1099, 3));
		when(productService.getAllProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/all")
												.exchange().expectStatus().isOk()
												.returnResult(ProductDto.class)
												.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto(1, "mobile", 599, 2))
				.expectNext(new ProductDto(2, "laptop", 1099, 3))
				.verifyComplete();
	}

	@Test
	public void testGetProductById() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(1, "mobile", 599, 2));
		when(productService.getProductById(1)).thenReturn(productDtoMono);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/find/1")
											.exchange().returnResult(ProductDto.class)
											.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> 1 == p.getId())
				.verifyComplete();
	}

	@Test
	public void testDeleteProduct() {
		when(productService.deleteProduct(1)).thenReturn(Mono.empty());
		webTestClient.delete().uri("/products/delete/1")
				.exchange()
				.expectStatus().isOk();
	}
}
