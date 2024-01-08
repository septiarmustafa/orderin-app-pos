package com.enigma.orderin.service;

import com.enigma.orderin.entity.ProductDetail;

public interface ProductDetailService {
   ProductDetail create (ProductDetail product);
   ProductDetail getById (Integer id);
   ProductDetail findProductDetailIsActive(Integer productId, Boolean isActive);
}
