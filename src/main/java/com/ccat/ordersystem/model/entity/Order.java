package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Order{
        @Id
        @GeneratedValue
        private long id;

        @ElementCollection
        @CollectionTable(name="ORDER_LINE",
                joinColumns ={@JoinColumn(name="order_id", referencedColumnName = "id")})
        private List<OrderLine> orderLineList;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id", referencedColumnName = "id")
        private Customer customer;
        private LocalDate dateOfSubmission;
}
