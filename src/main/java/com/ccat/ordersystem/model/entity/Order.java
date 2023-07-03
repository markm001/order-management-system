package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Order {
        @Id
        @GeneratedValue
        private long id;

        @ElementCollection
        @CollectionTable(name="ORDER_LINE",
                joinColumns ={@JoinColumn(name="order_id", referencedColumnName = "id")})
        private List<OrderLine> orderLineList;

        @ManyToOne(cascade = CascadeType.REMOVE)
        @JoinColumn(name = "customer_id", referencedColumnName = "id")
        private Customer customer;

        @Temporal(TemporalType.DATE)
        private LocalDate dateOfSubmission;


        public Order() { }

        public Order(long id, List<OrderLine> orderLineList, Customer customer, LocalDate dateOfSubmission) {
                this.id = id;
                this.orderLineList = orderLineList;
                this.customer = customer;
                this.dateOfSubmission = dateOfSubmission;
        }

        public long getId() {
                return id;
        }

        public List<OrderLine> getOrderLineList() {
                return orderLineList;
        }

        public Customer getCustomer() {
                return customer;
        }

        public LocalDate getDateOfSubmission() {
                return dateOfSubmission;
        }
}
