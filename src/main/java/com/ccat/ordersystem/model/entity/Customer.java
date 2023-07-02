package com.ccat.ordersystem.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Customer {
        @Id
        @GeneratedValue
        private long id;
        private String registrationCode;
        private String fullName;
        private String email;
        private String telephone;
}
