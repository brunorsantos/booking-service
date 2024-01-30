package com.technical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "BLOCK")
public class BlockEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;
    private UUID propertyId;
    private String reason;

    public BlockEntity(UUID id, LocalDate startDate, LocalDate endDate, UUID propertyId, String reason) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.propertyId = propertyId;
        this.reason = reason;
    }

    public BlockEntity() {
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
