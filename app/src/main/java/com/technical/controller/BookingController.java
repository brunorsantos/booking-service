package com.technical.controller;


import com.technical.BookingService;
import com.technical.model.Booking;
import com.technical.model.BookingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/properties/{propertyId}/bookings")
@Slf4j
public class BookingController {

    BookingService bookingService;

    BookingMapper mapper;

    @Autowired
    public BookingController(final BookingService bookingService, final BookingMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(@PathVariable UUID propertyId) {
        return new ResponseEntity<>(bookingService.getBookingsByPropertyId(propertyId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable UUID propertyId, @PathVariable UUID id) {
        return new ResponseEntity<>(bookingService.getBooking(propertyId, id), HttpStatus.OK);
    }


}
