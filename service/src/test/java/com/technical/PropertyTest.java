package com.technical;

import com.technical.model.Booking;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertFalse(subject.isBooked(new Date(), new Date()));

        subject.setBookings(List.of());
        assertFalse(subject.isBooked(new Date(), new Date()));
    }

    @Test
    void isBookedShouldReturnFalseWhenNewNoOverlap() {

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 4);
        final var at4 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at5 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        final var at7 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 3);
        final var at10 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        final var at17 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 3);
        final var at20 = calendar.getTime();



        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID());
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID());
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID());



        subject.setBookings(List.of(booking1, booking2, booking3));

        assertFalse(subject.isBooked(at7, at10));
        assertFalse(subject.isBooked(at10, at17));
    }

    @Test
    void isBookedShouldReturnTrueWhenOverlap() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 4);
        final var at4 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at5 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at6 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var at7 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 3);
        final var at10 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        final var at17 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 3);
        final var at20 = calendar.getTime();

        Booking booking1 = new Booking(UUID.randomUUID(), at1, at4, "Guest name1", "2", UUID.randomUUID());
        Booking booking2 = new Booking(UUID.randomUUID(), at5, at7, "Guest name2", "2", UUID.randomUUID());
        Booking booking3 = new Booking(UUID.randomUUID(), at17, at20, "Guest name2", "2", UUID.randomUUID());

        subject.setBookings(List.of(booking1, booking2, booking3));

        assertTrue(subject.isBooked(at6, at10));
    }

}
