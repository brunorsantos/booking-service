package com.technical.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Booking {
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String guestName;
    private String numberOfGuests;
    private UUID propertyId;

    public Booking(UUID id, LocalDate startDate, LocalDate endDate, String guestName, String numberOfGuests, UUID propertyId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestName = guestName;
        this.numberOfGuests = numberOfGuests;
        this.propertyId = propertyId;
    }

    public Booking() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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
