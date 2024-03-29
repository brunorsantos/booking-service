package com.technical.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Property {

    private UUID id;
    private String address;
    private String city;
    private String ownerName;
    private List<Booking> bookings;

    private List<Block> blocks;

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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    /**
     * Checks if the property is booked for a given date range.
     *
     * Obs: This method consider that a new book can start in the same day that another book ends.
     * Considering for example check-in and check-out hours,
     * with proper distances to use the same day
     *
     * @param startDate the start date of the date range to be checked
     * @param endDate the end date of the date range to be checked
     * @return true if the property is booked for any part of the date range, false otherwise
     */
    public boolean isBooked(LocalDate startDate, LocalDate endDate, UUID currentBookingId) {
        if (bookings == null || bookings.isEmpty()) {
            return false;
        }

        return bookings.stream()
                .filter(booking -> booking.getBookingState() == BookingState.ACTIVE)
                .filter(booking -> !booking.getId().equals(currentBookingId))
                .anyMatch(booking -> !(!booking.getEndDate().isAfter(startDate)
                        || !booking.getStartDate().isBefore(endDate)));

    }

    public boolean isBooked(LocalDate startDate, LocalDate endDate) {
        return isBooked(startDate, endDate, null);
    }

    public boolean isBlocked(LocalDate startDate, LocalDate endDate, UUID currentBlockId) {
        if (blocks == null || blocks.isEmpty()) {
            return false;
        }

        return blocks.stream()
                .filter(block -> !block.getId().equals(currentBlockId))
                .anyMatch(block -> !(!block.getEndDate().isAfter(startDate)
                        || !block.getStartDate().isBefore(endDate)));
    }

    public boolean isBlocked(LocalDate startDate, LocalDate endDate) {
        return isBlocked(startDate, endDate, null);
    }
}
