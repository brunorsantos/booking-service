package com.technical.controller;


import com.technical.BookingService;
import com.technical.dto.BookingDto;
import com.technical.dto.BookingDtoMapper;
import com.technical.model.Booking;
import com.technical.model.BookingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/properties/{propertyId}/bookings")
@Slf4j
public class BookingController {

    BookingService bookingService;

    BookingDtoMapper mapper;

    @Autowired
    public BookingController(final BookingService bookingService, final BookingDtoMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings(@PathVariable UUID propertyId) {
        final var bookings = bookingService.getBookingsByPropertyId(propertyId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable UUID propertyId, @PathVariable UUID id) {
        return new ResponseEntity<>(mapper.toDto(bookingService.getBooking(propertyId, id)), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(@PathVariable UUID propertyId, @RequestBody BookingDto booking) {
        return new ResponseEntity<>(bookingService.createBooking(propertyId, mapper.toBusiness(booking)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable UUID propertyId, @PathVariable UUID id, @RequestBody BookingDto booking) {
        return new ResponseEntity<>(bookingService.updateBooking(propertyId, id, mapper.toBusiness(booking)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID propertyId, @PathVariable UUID id) {
        bookingService.deleteBooking(propertyId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
