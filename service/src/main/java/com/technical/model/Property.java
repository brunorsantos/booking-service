package com.technical.model;

import java.util.UUID;

public class Property {

    private UUID id;
    private String address;
    private String city;
    private String ownerName;

    public Property(UUID id, String address, String city, String ownerName) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.ownerName = ownerName;
    }

    public Property() {
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
