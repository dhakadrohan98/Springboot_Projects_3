package org.codeship.ProductMicroservice.controller;

import org.codeship.ProductMicroservice.dto.CreateProductRestModel;
import org.codeship.ProductMicroservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRestModel createProductRestModel) {
        String productid = this.productService.createProduct(createProductRestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productid);
    }
}
