package com.technical;

import com.technical.model.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<Booking> getBookingsByPropertyId(UUID propertyId);


}
