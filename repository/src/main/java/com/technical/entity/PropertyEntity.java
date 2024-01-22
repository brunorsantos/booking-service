package com.technical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "PROPERTY")
public class PropertyEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String address;
    private String city;

    //For simplicity in this test, I am not using a separate table for owner
    private String ownerName;

    public PropertyEntity(UUID id, String address, String city, String ownerName) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.ownerName = ownerName;
    }

    public PropertyEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
