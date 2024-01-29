package com.technical;

import com.technical.model.Booking;
import com.technical.model.BookingState;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PropertyTest {

    private Property subject;
    private Calendar calendar;


    @BeforeEach
    void setUp() {
        subject = new Property();
        calendar = Calendar.getInstance();
    }
    @Test
    void isBookedShouldBeFalseWhenNoBookings() {

        subject.setBookings(null);
        assertFalse(subject.isBooked(LocalDate.now(), LocalDate.now()));

        subject.setBookings(List.of());
        assertFalse(subject.isBooked(LocalDate.now(), LocalDate.now()));
    }

    @Test
    void isBookedShouldReturnFalseWhenNewNoOverlap() {

        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at4 = referenceDate.plusDays(4);
        final var at5 = referenceDate.plusDays(5);
        final var at7 = referenceDate.plusDays(7);
        final var at10 = referenceDate.plusDays(10);
        final var at17 = referenceDate.plusDays(17);
        final var at20 = referenceDate.plusDays(20);

        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);



        subject.setBookings(List.of(booking1, booking2, booking3));

        assertFalse(subject.isBooked(at7, at10));
        assertFalse(subject.isBooked(at10, at17));
    }

    @Test
    void isBookedShouldReturnTrueWhenOverlap() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at4 = referenceDate.plusDays(4);
        final var at5 = referenceDate.plusDays(5);
        final var at6 = referenceDate.plusDays(5);
        final var at7 = referenceDate.plusDays(7);
        final var at10 = referenceDate.plusDays(10);
        final var at17 = referenceDate.plusDays(17);
        final var at20 = referenceDate.plusDays(20);

        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);

        subject.setBookings(List.of(booking1, booking2, booking3));

        assertTrue(subject.isBooked(at6, at10));
    }

    @Test
    void isBookedShouldReturnFalseWhenOverlapWithCancelledState() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at4 = referenceDate.plusDays(4);
        final var at5 = referenceDate.plusDays(5);
        final var at6 = referenceDate.plusDays(5);
        final var at7 = referenceDate.plusDays(7);
        final var at10 = referenceDate.plusDays(10);
        final var at17 = referenceDate.plusDays(17);
        final var at20 = referenceDate.plusDays(20);

        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID(), BookingState.CANCELLED);
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);

        subject.setBookings(List.of(booking1, booking2, booking3));

        assertFalse(subject.isBooked(at6, at10));
    }

    @Test
    void isBookedShouldReturnFalseWhenOverlapWithSameBooking() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at4 = referenceDate.plusDays(4);
        final var at5 = referenceDate.plusDays(5);
        final var at6 = referenceDate.plusDays(5);
        final var at7 = referenceDate.plusDays(7);
        final var at10 = referenceDate.plusDays(10);
        final var at17 = referenceDate.plusDays(17);
        final var at20 = referenceDate.plusDays(20);

        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID(), BookingState.ACTIVE);

        subject.setBookings(List.of(booking1, booking2, booking3));

        assertFalse(subject.isBooked(at6, at10, booking2.getId()));
    }

}
