package com.ccat.ordersystem.model.repository;

import com.ccat.ordersystem.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.dateOfSubmission BETWEEN ?1 AND ?2")
    List<Order> findAllByDateOfSubmission(LocalDate lower, LocalDate upper);

    @Query("SELECT o FROM Order o JOIN o.orderLineList l WHERE l.product.id = ?1")
    List<Order> findAllByProductId(Long productId);

    @Query("SELECT o FROM Order o JOIN o.customer c WHERE c.id = ?1")
    List<Order> findAllByCustomerId(Long customerId);
}
