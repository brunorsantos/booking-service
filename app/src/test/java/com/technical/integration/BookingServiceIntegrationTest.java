package com.technical.integration;

import com.technical.BookingRepository;
import com.technical.BookingServiceImpl;
import com.technical.PropertyService;
import com.technical.entity.BookingEntity;
import com.technical.entity.BookingEntityState;
import com.technical.exception.ConflictedDateException;
import com.technical.model.Booking;
import com.technical.model.BookingState;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BookingServiceImpl subject;

    @Autowired
    public BookingServiceIntegrationTest(final BookingRepository bookingRepository, final PropertyService propertyService) {
        subject = new BookingServiceImpl(bookingRepository, bookingMapper, propertyService);
    }

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
    }

    @Test
    void shouldGetBookingsByPropertyId() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);


        final var booking1 = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", property.getId(), BookingState.ACTIVE);
        final var booking2 = new Booking(UUID.randomUUID(), startDate2, endDate2, "Guest name1", "5", property.getId(), BookingState.ACTIVE);

        final var bookingEntities = List.of(new BookingEntity(booking1.getId(), booking1.getStartDate(), booking1.getEndDate(), booking1.getGuestName(), booking1.getNumberOfGuests(), booking1.getPropertyId(), BookingEntityState.ACTIVE),
                new BookingEntity(booking2.getId(), booking2.getStartDate(), booking2.getEndDate(), booking2.getGuestName(), booking2.getNumberOfGuests(), booking2.getPropertyId(), BookingEntityState.ACTIVE));

        bookingRepository.saveAll(bookingEntities);

        final var bookings = subject.getBookingsByPropertyId(property.getId());

        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0).getStartDate()).isEqualTo(booking1.getStartDate());
        assertThat(bookings.get(0).getEndDate()).isEqualTo(booking1.getEndDate());
        assertThat(bookings.get(0).getGuestName()).isEqualTo(booking1.getGuestName());
        assertThat(bookings.get(0).getNumberOfGuests()).isEqualTo(booking1.getNumberOfGuests());
        assertThat(bookings.get(0).getBookingState()).isEqualTo(booking1.getBookingState());
    }

    @Test
    void shouldCreateBooking() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), startDate, endDate, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);

        subject.createBooking(savedProperty.getId(), booking);

        final var bookings = subject.getBookingsByPropertyId(savedProperty.getId());

        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0).getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookings.get(0).getEndDate()).isEqualTo(booking.getEndDate());
        assertThat(bookings.get(0).getGuestName()).isEqualTo(booking.getGuestName());
        assertThat(bookings.get(0).getNumberOfGuests()).isEqualTo(booking.getNumberOfGuests());
        assertThat(bookings.get(0).getBookingState()).isEqualTo(booking.getBookingState());

    }

    @Test
    void shouldUpdateBooking() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), startDate, endDate, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);
        final var savedBooking = bookingRepository.save(bookingMapper.toEntity(booking));

        final var updatedStartDate = referenceDate.plusDays(2);
        final var updatedEndDate = referenceDate.plusDays(6);

        final var updatedBooking = new Booking(savedBooking.getId(), updatedStartDate, updatedEndDate, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);

        subject.updateBooking(savedProperty.getId(), savedBooking.getId(), updatedBooking);

        final var bookings = subject.getBookingsByPropertyId(savedProperty.getId());

        assertThat(bookings.size()).isEqualTo(1);
        assertThat(bookings.get(0).getStartDate()).isEqualTo(updatedBooking.getStartDate());
        assertThat(bookings.get(0).getEndDate()).isEqualTo(updatedBooking.getEndDate());
        assertThat(bookings.get(0).getGuestName()).isEqualTo(updatedBooking.getGuestName());
        assertThat(bookings.get(0).getNumberOfGuests()).isEqualTo(updatedBooking.getNumberOfGuests());
        assertThat(bookings.get(0).getBookingState()).isEqualTo(updatedBooking.getBookingState());
    }

    @Test
    void shouldNotUpdateBookingOverlapping() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), at1, at3, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);
        final var savedBooking = bookingRepository.save(bookingMapper.toEntity(booking));

        final var otherBooking = new Booking(UUID.randomUUID(), at3, at5, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);
        bookingRepository.save(bookingMapper.toEntity(otherBooking));


        final var updatedBooking = new Booking(savedBooking.getId(), at1, at5, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);

        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBooking(savedProperty.getId(), savedBooking.getId(), updatedBooking);
        });
    }

    @Test
    void shouldDeleteBooking() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), startDate, endDate, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);
        final var savedBooking = bookingRepository.save(bookingMapper.toEntity(booking));

        subject.deleteBooking(savedProperty.getId(), savedBooking.getId());

        final var bookings = subject.getBookingsByPropertyId(savedProperty.getId());

        assertThat(bookings.size()).isEqualTo(0);
    }


}
