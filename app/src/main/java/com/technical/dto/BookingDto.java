package com.technical.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class BookingDto {
    private UUID id;
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "Guest name cannot be null")
    private String guestName;

    @NotNull(message = "Number of guests cannot be null")
    private String numberOfGuests;

    private UUID propertyId;

    @NotNull(message = "State Id cannot be null")
    private BookingStateDto bookingState;

    public BookingDto() {
    }

    @JsonProperty
    public UUID getId() {
        return id;
    }
    @JsonIgnore
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

    @JsonProperty
    public UUID getPropertyId() {
        return propertyId;
    }

    @JsonIgnore
    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public BookingStateDto getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingStateDto bookingState) {
        this.bookingState = bookingState;
    }
}
