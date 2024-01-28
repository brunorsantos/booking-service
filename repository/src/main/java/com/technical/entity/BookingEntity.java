package com.technical.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "BOOKING")
public class BookingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private Date startDate;

    private Date endDate;

    //For simplicity in this test, I am not using a separate table for Guests users
    private String guestName;

    private String numberOfGuests;

    private UUID propertyId;

    public BookingEntity(UUID id, Date startDate, Date endDate, String guestName, String numberOfGuests, UUID propertyId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestName = guestName;
        this.numberOfGuests = numberOfGuests;
        this.propertyId = propertyId;
    }

    public BookingEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(String numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }
}
