package com.technical;

import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.integration.AbstractIntegrationTest;
import com.technical.model.Booking;
import com.technical.model.BookingState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookingControllerTest extends AbstractIntegrationTest {

    @MockBean
    BookingService bookingService;

    @Test
    void shouldGetAllBookings() throws Exception {

        final var propertyId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);

        final var booking1 = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);
        final var booking2 = new Booking(UUID.randomUUID(), startDate2, endDate2, "Guest name1", "5", propertyId, BookingState.ACTIVE);



        when(bookingService.getBookingsByPropertyId(any(UUID.class))).thenReturn(List.of(booking1, booking2));

        mockMvc.perform(get("/properties/" + propertyId + "/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startDate").value(startDate1.toString()))
                .andExpect(jsonPath("$[0].endDate").value(endDate1.toString()))
                .andExpect(jsonPath("$[0].guestName").value(booking1.getGuestName()))
                .andExpect(jsonPath("$[0].numberOfGuests").value(booking1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].propertyId").value(booking1.getPropertyId().toString()))
                .andExpect(jsonPath("$[0].id").value(booking1.getId().toString()))
                .andExpect(jsonPath("$[1].startDate").value(startDate2.toString()))
                .andExpect(jsonPath("$[1].endDate").value(endDate2.toString()))
                .andExpect(jsonPath("$[1].guestName").value(booking2.getGuestName()))
                .andExpect(jsonPath("$[1].numberOfGuests").value(booking2.getNumberOfGuests()))
                .andExpect(jsonPath("$[1].propertyId").value(booking2.getPropertyId().toString()))
                .andExpect(jsonPath("$[1].id").value(booking2.getId().toString()));
    }

    @Test
    void shouldGetBooking() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var booking1 = new Booking(bookingId, startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);

        when(bookingService.getBooking(any(UUID.class), any(UUID.class))).thenReturn(booking1);

        mockMvc.perform(get("/properties/" + propertyId + "/bookings/" + bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value(startDate1.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate1.toString()))
                .andExpect(jsonPath("$.guestName").value(booking1.getGuestName()))
                .andExpect(jsonPath("$.numberOfGuests").value(booking1.getNumberOfGuests()))
                .andExpect(jsonPath("$.propertyId").value(booking1.getPropertyId().toString()))
                .andExpect(jsonPath("$.id").value(booking1.getId().toString()));
    }

    @Test
    void shouldGetBookingNotFound() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        when(bookingService.getBooking(any(UUID.class), any(UUID.class))).thenThrow(new ResourceNotFoundException("Booking not found"));

        mockMvc.perform(get("/properties/" + propertyId + "/bookings/" + bookingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBooking() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var booking1 = new Booking(bookingId, startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);

        when(bookingService.createBooking(any(UUID.class), any(Booking.class))).thenReturn(booking1);

        mockMvc.perform(post("/properties/" + propertyId + "/bookings")
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate1 + "\", \"endDate\": \"" + endDate1 + "\", \"guestName\": \"Guest name1\", \"numberOfGuests\": \"2\", \"bookingState\": \"ACTIVE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startDate").value(startDate1.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate1.toString()))
                .andExpect(jsonPath("$.guestName").value(booking1.getGuestName()))
                .andExpect(jsonPath("$.numberOfGuests").value(booking1.getNumberOfGuests()))
                .andExpect(jsonPath("$.propertyId").value(booking1.getPropertyId().toString()))
                .andExpect(jsonPath("$.id").value(booking1.getId().toString()));
    }

    @Test
    void shouldUpdateBooking() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var booking1 = new Booking(bookingId, startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);

        when(bookingService.updateBooking(any(UUID.class), any(UUID.class), any(Booking.class))).thenReturn(booking1);

        mockMvc.perform(put("/properties/" + propertyId + "/bookings/" + bookingId)
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate1 + "\", \"endDate\": \"" + endDate1 + "\", \"guestName\": \"Guest name1\", \"numberOfGuests\": \"2\", \"bookingState\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value(startDate1.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate1.toString()))
                .andExpect(jsonPath("$.guestName").value(booking1.getGuestName()))
                .andExpect(jsonPath("$.numberOfGuests").value(booking1.getNumberOfGuests()))
                .andExpect(jsonPath("$.propertyId").value(booking1.getPropertyId().toString()))
                .andExpect(jsonPath("$.id").value(booking1.getId().toString()));
    }

    @Test
    void shouldUpdateBookingNotFound() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        when(bookingService.updateBooking(any(UUID.class), any(UUID.class), any(Booking.class))).thenThrow(new ResourceNotFoundException("Booking not found"));

        mockMvc.perform(put("/properties/" + propertyId + "/bookings/" + bookingId)
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate1 + "\", \"endDate\": \"" + endDate1 + "\", \"guestName\": \"Guest name1\", \"numberOfGuests\": \"2\", \"bookingState\": \"ACTIVE\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateBookingInvalidDates() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        when(bookingService.updateBooking(any(UUID.class), any(UUID.class), any(Booking.class))).thenThrow(new IllegalArgumentException("Invalid dates"));

        mockMvc.perform(put("/properties/" + propertyId + "/bookings/" + bookingId)
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate1 + "\", \"endDate\": \"" + endDate1 + "\", \"guestName\": \"Guest name1\", \"numberOfGuests\": \"2\", \"bookingState\": \"ACTIVE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBookingConflictedDates() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        when(bookingService.updateBooking(any(UUID.class), any(UUID.class), any(Booking.class))).thenThrow(new ConflictedDateException("Property is already booked for the selected dates"));

        mockMvc.perform(put("/properties/" + propertyId + "/bookings/" + bookingId)
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate1 + "\", \"endDate\": \"" + endDate1 + "\", \"guestName\": \"Guest name1\", \"numberOfGuests\": \"2\", \"bookingState\": \"ACTIVE\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDeleteBooking() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        mockMvc.perform(delete("/properties/" + propertyId + "/bookings/" + bookingId))
                .andExpect(status().isNoContent());
    }

    //TODO: Add tests for validation errors
}
