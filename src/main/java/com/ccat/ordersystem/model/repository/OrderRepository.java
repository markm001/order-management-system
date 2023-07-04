package com.ccat.ordersystem.model.repository;

import com.ccat.ordersystem.model.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.dateOfSubmission BETWEEN ?1 AND ?2")
    List<Order> findAllByDateOfSubmission(LocalDate lower, LocalDate upper);

    @Query("SELECT o FROM Order o JOIN o.orderLineList l WHERE l.product.id = ?1")
    List<Order> findAllByProductId(Long productId);

    @Query("SELECT o FROM Order o JOIN o.customer c WHERE c.id = ?1")
    List<Order> findAllByCustomerId(Long customerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"order_line\" SET \"quantity\"=:quantity WHERE \"id\"=:orderLineId", nativeQuery = true)
    int updateOrderLineById(Long orderLineId, int quantity);
}
