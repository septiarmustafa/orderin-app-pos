package com.enigma.orderin.repository;

import com.enigma.orderin.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Modifying()
    @Query(value = "INSERT INTO t_order_detail (order_id, product_detail_id, quantity) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void createOrderDetail(Integer orderId, Integer productDetailId, Integer quantity);

    @Query(value = "SELECT * FROM t_order_detail WHERE id IN (SELECT MAX(id) FROM t_order_detail)", nativeQuery = true)
    Integer getLastInsertedId();

}
