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
    @Query(value = "INSERT INTO t_product_detail (price, stock, is_active, product_id) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void createNewProductDetail(Long price,Integer stock, Boolean isActive,  Integer productId);

    @Query(value = "SELECT * FROM t_product_detail WHERE id IN (SELECT MAX(id) FROM t_product_detail)", nativeQuery = true)
    Long getLastInsertedId();

    @Query(value = "SELECT * FROM t_product_detail pd WHERE pd.id = ?1", nativeQuery = true)
    Optional<ProductDetail> findById(Integer id);

    @Query(value = "SELECT * FROM t_product_detail pd WHERE pd.product_id = ?1 AND pd.is_active = ?2", nativeQuery = true)
    Optional<ProductDetail> findByProduct_IdAndIsActive( Integer productId, Boolean isActive);
}
