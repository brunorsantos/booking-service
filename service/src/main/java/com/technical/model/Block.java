package com.technical.model;

import java.time.LocalDate;
import java.util.UUID;

public class Block {

    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;
    private UUID propertyId;
    private String reason;

    public Block(UUID id, LocalDate startDate, LocalDate endDate, UUID propertyId, String reason) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.propertyId = propertyId;
        this.reason = reason;
    }

    public Block() {
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

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
