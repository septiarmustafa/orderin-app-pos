package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.request.ProductRequest;
import com.enigma.orderin.dto.response.ProductResponse;
import com.enigma.orderin.entity.Product;
import com.enigma.orderin.entity.ProductDetail;
import com.enigma.orderin.repository.ProductRepository;
import com.enigma.orderin.service.ProductDetailService;
import com.enigma.orderin.service.ProductService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailService productDetailService;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRequest == null || StringUtils.isBlank(productRequest.getProductName())) {
            throw new IllegalArgumentException("Product name must not be empty or null");
        }
        productRepository.createNewProduct(productRequest.getProductName());
        Long lastInsertId = productRepository.getLastInsertedId();

        if (lastInsertId  == 0) {
            throw new RuntimeException("Failed to create the product");
        }

        return ProductResponse.builder()
                .productId(lastInsertId.intValue())
                .productName(productRequest.getProductName())
                .isActive(true)
                .build();
    }
    @Override
    public ProductResponse getByIdProduct(Integer id) {

        Optional<Product> productOptional = productRepository.findByIdProduct(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ProductResponse.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public List<Product> getAllProduct() {

        List<Product> productList = productRepository.findAllProducts();
        return productList.stream()
                .map(product -> Product.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .productDetails(product.getProductDetails())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest) {
        if (productRequest == null || productRequest.getProductId() == null || StringUtils.isBlank(productRequest.getProductName())) {
            throw new IllegalArgumentException("Product ID and name must not be empty or null");
        }
        Product product = productRepository.findByIdProduct(productRequest.getProductId()).orElse(null);
        if (product == null) return null;

        productRepository.updateProductName(productRequest.getProductId(), productRequest.getProductName());
        Product productUpdated = productRepository.findByIdProduct(productRequest.getProductId()).orElse(null);
        if (productUpdated == null) return null;

        return ProductResponse.builder()
                .productId(productUpdated.getId())
                .productName(productUpdated.getName())
                .build();
    }

    @Override
    public void deleteProduct(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        productRepository.deleteByIdProduct(id);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse createProductAndProductPrice(ProductRequest productRequest) {

        ProductResponse productResponse = createProduct(productRequest);

        Product product = Product.builder()
                .id(productResponse.getProductId())
                .name(productResponse.getProductName())
                .build();

        Long lastInsertId = productRepository.getLastInsertedId();

        if (lastInsertId  == 0) {
            throw new RuntimeException("Failed to create the product");
        }

        ProductDetail productDetail = ProductDetail.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .isActive(true)
                .product(product)
                .build();

        productDetailService.create(productDetail);
        return ProductResponse.builder()
                .productId(lastInsertId.intValue())
                .productName(product.getName())
                .price(productDetail.getPrice())
                .isActive(true)
                .stock(productDetail.getStock())
                .build();
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            Join<Product, ProductDetail> productDetailJoin = root.join("productDetail");
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%" ));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productDetailJoin.get("price"), maxPrice));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> products = productRepository.findAll(specification,pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            Optional<ProductDetail> productDetail = product.getProductDetails()
                    .stream()
                    .filter(ProductDetail::getIsActive).findFirst();
            if (productDetail.isEmpty()) continue;
            productResponses.add(
                    ProductResponse.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .price(productDetail.get().getPrice())
                            .stock(productDetail.get().getStock())

                    .build());
        }
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }
}
