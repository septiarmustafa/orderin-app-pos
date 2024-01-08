package com.enigma.orderin.repository;

import com.enigma.orderin.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO t_product_detail (price, stock, is_active, product_id) VALUES (:price, :stock, :isActive, :productId)", nativeQuery = true)
    void createNewProductDetail(@Param("price") Long price, @Param("stock") Integer stock, @Param("isActive") Boolean isActive, @Param("productId") Integer productId);

    @Query(value = "SELECT * FROM t_product_detail WHERE id IN (SELECT MAX(id) FROM t_product_detail)", nativeQuery = true)
    Long getLastInsertedId();
    @Query(value = "SELECT * FROM t_product_detail pd WHERE pd.id = :id", nativeQuery = true)
    Optional<ProductDetail> findById(@Param("id") Integer id);
    @Query(value = "SELECT * FROM t_product_detail pd WHERE pd.product_id = :productId AND pd.is_active = :isActive", nativeQuery = true)
    Optional<ProductDetail> findByProduct_IdAndIsActive(@Param("productId") Integer productId, @Param("isActive") Boolean isActive);
//    Optional<ProductDetail> findByProduct_IdAndIsActive(Integer productId, Boolean isActive);
}
