package org.codeship.ProductMicroservice.service;

import org.codeship.ProductMicroservice.dto.CreateProductRestModel;

public interface ProductService {

    public String createProduct(CreateProductRestModel createProductRestModel) throws Exception;
}
