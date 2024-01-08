package com.enigma.orderin.repository;

import com.enigma.orderin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Modifying()
    @Query(value = "INSERT INTO t_order (date, cashier_id) VALUES (?1, ?2)", nativeQuery = true)
    void createNewOrder(LocalDate orderDate,Integer cashierId);

    @Query(value = "SELECT * FROM t_order WHERE id IN (SELECT MAX(id) FROM t_order)", nativeQuery = true)
    Integer getLastInsertedId();

    @Query(value = "SELECT * FROM t_order o WHERE o.date = ?1", nativeQuery = true)
    List<Order> findAllByDate(LocalDate orderDate);
    @Query(value = "SELECT * FROM t_order WHERE id = ?1", nativeQuery = true)
    Optional<Order> getOrderById(Integer id);

    @Query(value = "SELECT * FROM t_order", nativeQuery = true)
    List<Order> getAllOrder();
}
