package com.maksymfedosov.entity;


import lombok.Data;

@Data
public class Order {

    private long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete = false;

    public Order(long petId) {
        this.petId = petId;
    }
}
