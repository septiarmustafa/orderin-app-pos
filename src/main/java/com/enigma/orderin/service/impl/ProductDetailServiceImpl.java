package com.enigma.orderin.service.impl;


import com.enigma.orderin.entity.ProductDetail;
import com.enigma.orderin.repository.ProductDetailRepository;
import com.enigma.orderin.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public ProductDetail create(ProductDetail productDetail) {

       productDetailRepository.createNewProductDetail(
               productDetail.getPrice(),
               productDetail.getStock(),
               productDetail.getIsActive(),
               productDetail.getProduct().getId());

        Long lastInsertId = productDetailRepository.getLastInsertedId();

        if (lastInsertId  == 0) {
            throw new RuntimeException("Failed to create the product");
        }
        return ProductDetail.builder()
                .id(productDetail.getId())
                .price(productDetail.getPrice())
                .stock(productDetail.getStock())
                .isActive(true)
                .build();
    }

    @Override
    public ProductDetail getById(Integer id) {
        return productDetailRepository.findById(id).orElseThrow();
    }

    @Override
    public ProductDetail findProductDetailIsActive(Integer productId, Boolean isActive) {
        return productDetailRepository.findByProduct_IdAndIsActive(productId, isActive).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
