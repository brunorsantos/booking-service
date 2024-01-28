package com.technical.integration;

import com.technical.BookingRepository;
import com.technical.BookingServiceImpl;
import com.technical.entity.BookingEntity;
import com.technical.model.Booking;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookingServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BookingServiceImpl subject;

    @Autowired
    public BookingServiceIntegrationTest(final BookingRepository bookingRepository) {
        subject = new BookingServiceImpl(bookingRepository, bookingMapper);
    }

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
    }

    @Test
    void shouldGetBookingsByPropertyId() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        propertyRepository.save(propertyMapper.toEntity(property));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date startDate1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 4);
        Date endDate1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 5);
        Date startDate2 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 10);
        Date endDate2 = calendar.getTime();


        final var booking1 = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", property.getId());
        final var booking2 = new Booking(UUID.randomUUID(), startDate2, endDate2, "Guest name1", "5", property.getId());

        final var bookingEntities = List.of(new BookingEntity(booking1.getId(), booking1.getStartDate(), booking1.getEndDate(), booking1.getGuestName(), booking1.getNumberOfGuests(), booking1.getPropertyId()),
                new BookingEntity(booking2.getId(), booking2.getStartDate(), booking2.getEndDate(), booking2.getGuestName(), booking2.getNumberOfGuests(), booking2.getPropertyId()));

        bookingRepository.saveAll(bookingEntities);

        final var bookings = subject.getBookingsByPropertyId(property.getId());

        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0).getStartDate()).isEqualTo(booking1.getStartDate());
        assertThat(bookings.get(0).getEndDate()).isEqualTo(booking1.getEndDate());
        assertThat(bookings.get(0).getGuestName()).isEqualTo(booking1.getGuestName());
        assertThat(bookings.get(0).getNumberOfGuests()).isEqualTo(booking1.getNumberOfGuests());




    }
}
