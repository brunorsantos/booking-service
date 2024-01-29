package com.technical;

import com.technical.model.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<Booking> getBookingsByPropertyId(UUID propertyId);

    Booking getBooking(UUID propertyId, UUID id);

    Booking createBooking(UUID propertyId, Booking booking);

    Booking updateBooking(UUID propertyId, UUID id, Booking booking);


}
