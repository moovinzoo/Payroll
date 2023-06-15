package com.example.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    private Status status;

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }
}
