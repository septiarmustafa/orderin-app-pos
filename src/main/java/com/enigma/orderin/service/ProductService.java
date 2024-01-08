package com.enigma.orderin.service;

import com.enigma.orderin.dto.request.ProductRequest;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct (ProductRequest productRequest);

    ProductResponse getByIdProduct (Integer id);

    List<Product> getAllProduct ();

    ProductResponse updateProduct (ProductRequest productRequest);
    void deleteProduct (Integer id);

    ProductResponse createProductAndProductPrice (ProductRequest productRequest);

    Page<ProductResponse> getAllByNameOrPrice (String name, Long maxPrice, Integer page, Integer size);
}
