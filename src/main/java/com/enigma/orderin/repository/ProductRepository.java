package com.enigma.orderin.repository;

import com.enigma.orderin.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO m_product (name) VALUES (?1)", nativeQuery = true)
    void createNewProduct(String productName);

    @Query(value = "SELECT * FROM m_product WHERE id IN (SELECT MAX(id) FROM m_product)", nativeQuery = true)
    Long getLastInsertedId();

    @Query(value = "SELECT * FROM m_product WHERE id = ?1", nativeQuery = true)
    Optional<Product> findByIdProduct(Integer id);

    @Query(value = "SELECT p.* FROM m_product p INNER JOIN t_product_detail pd ON p.id = pd.product_id WHERE pd.is_active = true", nativeQuery = true)
    List<Product> findAllProducts();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE m_product SET name = ?2 WHERE id = ?1", nativeQuery = true)
    void updateProductName(Integer id, String newName);

    @Modifying
    @Query(value = "UPDATE t_product_detail SET is_active = false WHERE id = ?1", nativeQuery = true)
    void deleteByIdProduct(Integer id);

}
