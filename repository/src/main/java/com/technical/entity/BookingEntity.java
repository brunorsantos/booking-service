package com.technical.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "BOOKING")
public class BookingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    //For simplicity in this test, I am not using a separate table for Guests users
    private String guestName;

    private String numberOfGuests;

    private UUID propertyId;

    private BookingEntityState bookingState;

    public BookingEntity(UUID id, LocalDate startDate, LocalDate endDate, String guestName, String numberOfGuests, UUID propertyId, BookingEntityState bookingState) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guestName = guestName;
        this.numberOfGuests = numberOfGuests;
        this.propertyId = propertyId;
        this.bookingState = bookingState;
    }

    public BookingEntity() {
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

    public BookingEntityState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingEntityState bookingEntityState) {
        this.bookingState = bookingEntityState;
    }
}
