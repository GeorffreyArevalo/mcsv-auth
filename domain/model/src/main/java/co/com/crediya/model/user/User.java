package co.com.crediya.model.user;

import java.math.BigDecimal;

public class User {

    private String name;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private BigDecimal basePayment;

    public User() {
    }

    public User(String name, String lastName, String email, String document, String phone, BigDecimal basePayment) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.document = document;
        this.phone = phone;
        this.basePayment = basePayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBasePayment() {
        return basePayment;
    }

    public void setBasePayment(BigDecimal basePayment) {
        this.basePayment = basePayment;
    }
}
