package com.ccat.ordersystem.model.entity;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = "registrationCode")
)
public class Customer {
        @Id
        @GeneratedValue
        private long id;
        private String registrationCode;
        private String fullName;
        private String email;
        private String telephone;

        public Customer() { }

        public Customer(long id, String registrationCode, String fullName, String email, String telephone) {
                this.id = id;
                this.registrationCode = registrationCode;
                this.fullName = fullName;
                this.email = email;
                this.telephone = telephone;
        }

        public long getId() {
                return id;
        }

        public String getRegistrationCode() {
                return registrationCode;
        }

        public String getFullName() {
                return fullName;
        }

        public String getEmail() {
                return email;
        }

        public String getTelephone() {
                return telephone;
        }
}
