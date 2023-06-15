package com.example.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    private String role;

    public Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String fullName) {
        String[] names = fullName.split(" ");
        this.firstName = names[0];
        this.lastName = names[1];
    }
}