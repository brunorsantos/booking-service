package com.technical;

import com.technical.entity.BookingEntity;
import com.technical.entity.PropertyEntity;
import com.technical.model.Booking;
import com.technical.model.BookingMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    private BookingService subject;
    private BookingRepository bookingRepositoryMock;


    @BeforeEach
    void setUp() {
        var mapper = new BookingMapperImpl();
        bookingRepositoryMock = mock(BookingRepository.class);
        subject = new BookingServiceImpl(bookingRepositoryMock, mapper);
    }

    @Test
    void shouldGetBookingsByPropertyId() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date startDate1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 4);
        Date endDate1 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 5);
        Date startDate2 = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 10);
        Date endDate2 = calendar.getTime();

        final var propertyId = UUID.randomUUID();
        final var booking1 = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", propertyId);
        final var booking2 = new Booking(UUID.randomUUID(), startDate2, endDate2, "Guest name1", "5", propertyId);

        final var bookingEntities = List.of(new BookingEntity(booking1.getId(), booking1.getStartDate(), booking1.getEndDate(), booking1.getGuestName(), booking1.getNumberOfGuests(), booking1.getPropertyId()),
                new BookingEntity(booking2.getId(), booking2.getStartDate(), booking2.getEndDate(), booking2.getGuestName(), booking2.getNumberOfGuests(), booking2.getPropertyId()));

        when(bookingRepositoryMock.findByPropertyId(propertyId)).thenReturn(bookingEntities);

        final var bookings = subject.getBookingsByPropertyId(propertyId);

        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.get(0).getId()).isEqualTo(booking1.getId());
        assertThat(bookings.get(0).getStartDate()).isEqualTo(booking1.getStartDate());
        assertThat(bookings.get(0).getEndDate()).isEqualTo(booking1.getEndDate());
        assertThat(bookings.get(0).getGuestName()).isEqualTo(booking1.getGuestName());
        assertThat(bookings.get(0).getNumberOfGuests()).isEqualTo(booking1.getNumberOfGuests());
        assertThat(bookings.get(0).getPropertyId()).isEqualTo(booking1.getPropertyId());
        assertThat(bookings.get(1).getId()).isEqualTo(booking2.getId());
        assertThat(bookings.get(1).getStartDate()).isEqualTo(booking2.getStartDate());
        assertThat(bookings.get(1).getEndDate()).isEqualTo(booking2.getEndDate());
        assertThat(bookings.get(1).getGuestName()).isEqualTo(booking2.getGuestName());
        assertThat(bookings.get(1).getNumberOfGuests()).isEqualTo(booking2.getNumberOfGuests());
    }
}
