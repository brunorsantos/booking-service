package com.technical;

import com.technical.entity.BookingEntity;
import com.technical.entity.BookingEntityState;
import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private BookingService subject;
    private BookingRepository bookingRepositoryMock;

    private PropertyService propertyServiceMock;

    private BookingMapperImpl bookingMapper;


    @BeforeEach
    void setUp() {
        bookingMapper = new BookingMapperImpl();
        bookingRepositoryMock = mock(BookingRepository.class);
        propertyServiceMock = mock(PropertyService.class);
        subject = new BookingServiceImpl(bookingRepositoryMock, bookingMapper, propertyServiceMock);
    }

    @Test
    void shouldGetBookingsByPropertyId() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);

        final var propertyId = UUID.randomUUID();
        final var booking1 = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);
        final var booking2 = new Booking(UUID.randomUUID(), startDate2, endDate2, "Guest name1", "5", propertyId, BookingState.ACTIVE);

        final var bookingEntities = List.of(new BookingEntity(booking1.getId(), booking1.getStartDate(), booking1.getEndDate(), booking1.getGuestName(), booking1.getNumberOfGuests(), booking1.getPropertyId(), BookingEntityState.ACTIVE),
                new BookingEntity(booking2.getId(), booking2.getStartDate(), booking2.getEndDate(), booking2.getGuestName(), booking2.getNumberOfGuests(), booking2.getPropertyId(), BookingEntityState.ACTIVE));

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

    @Test
    void shouldGetBooking() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", propertyId, BookingState.ACTIVE);

        final var bookingEntity = new BookingEntity(booking.getId(), booking.getStartDate(), booking.getEndDate(), booking.getGuestName(), booking.getNumberOfGuests(), booking.getPropertyId(), BookingEntityState.ACTIVE);

        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        final var returnedBooking = subject.getBooking(propertyId, booking.getId());

        assertThat(returnedBooking.getId()).isEqualTo(booking.getId());
        assertThat(returnedBooking.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(returnedBooking.getEndDate()).isEqualTo(booking.getEndDate());
        assertThat(returnedBooking.getGuestName()).isEqualTo(booking.getGuestName());
        assertThat(returnedBooking.getNumberOfGuests()).isEqualTo(booking.getNumberOfGuests());
        assertThat(returnedBooking.getPropertyId()).isEqualTo(booking.getPropertyId());
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFound() {
        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        when(bookingRepositoryMock.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.getBooking(propertyId, bookingId);
        });

    }

    @Test
    void shouldThrowExceptionWhenBookingFromDifferentProperty() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);

        final var bookingEntity = new BookingEntity(booking.getId(), booking.getStartDate(), booking.getEndDate(), booking.getGuestName(), booking.getNumberOfGuests(), booking.getPropertyId(), BookingEntityState.ACTIVE);

        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.getBooking(propertyId, booking.getId());
        });

    }

    @Test
    void shouldCreateBooking() {
        //Prepare
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBooking = new Booking(UUID.randomUUID(), at1, at3, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(mockedBooking));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        final var booking = new Booking(UUID.randomUUID(), at3, at5, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);
        final var bookingArgumentCaptor = ArgumentCaptor.forClass(BookingEntity.class);

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        //Execute
        subject.createBooking(mockedProperty.getId(), booking);

        //Assert
        verify(bookingRepositoryMock).save(bookingArgumentCaptor.capture());

        final var savedBookingEntity = bookingArgumentCaptor.getValue();

        assertThat(savedBookingEntity.getId()).isEqualTo(booking.getId());
        assertThat(savedBookingEntity.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(savedBookingEntity.getEndDate()).isEqualTo(booking.getEndDate());
        assertThat(savedBookingEntity.getGuestName()).isEqualTo(booking.getGuestName());
        assertThat(savedBookingEntity.getNumberOfGuests()).isEqualTo(booking.getNumberOfGuests());
        assertThat(savedBookingEntity.getPropertyId()).isEqualTo(booking.getPropertyId());
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(5);
        final var endDate1 = referenceDate.plusDays(1);

        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", property.getId(), BookingState.ACTIVE);

        assertThrows(IllegalArgumentException.class, () ->{
            subject.createBooking(property.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBooked() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at2 = referenceDate.plusDays(2);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBooking = new Booking(UUID.randomUUID(), at1, at3, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(mockedBooking));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var booking = new Booking(UUID.randomUUID(), at2, at5, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);


        assertThrows(ConflictedDateException.class, () ->{
            subject.createBooking(mockedProperty.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBlocked() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at2 = referenceDate.plusDays(2);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBlock = new Block(UUID.randomUUID(), at1, at3, mockedProperty.getId(), "Blocked for maintenance");
        mockedProperty.setBlocks(List.of(mockedBlock));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var booking = new Booking(UUID.randomUUID(), at2, at5, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);

        assertThrows(ConflictedDateException.class, () ->{
            subject.createBooking(mockedProperty.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenBookingIsNotActive() {
        final var referenceDate = LocalDate.now();
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), at3, at5, "Guest name1", "2", UUID.randomUUID(), BookingState.CANCELLED);

        assertThrows(IllegalArgumentException.class, () ->{
            subject.createBooking(UUID.randomUUID(), booking);
        });
    }

    @Test
    void shouldUpdateBookingDatesGuestNameAndNumberOfGuests() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var bookingToUpdate = new Booking(UUID.randomUUID(), at3, at5, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        final var mockedBookingToBeUpdatedEntity = new BookingEntity(bookingToUpdate.getId(), at1, bookingToUpdate.getEndDate(), "Old Guest Name", "2", bookingToUpdate.getPropertyId(), BookingEntityState.ACTIVE);


        mockedProperty.setBookings(List.of(bookingMapper.toBusiness(mockedBookingToBeUpdatedEntity)));

        when(bookingRepositoryMock.findById(bookingToUpdate.getId())).thenReturn(Optional.of(mockedBookingToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var bookingArgumentCaptor = ArgumentCaptor.forClass(BookingEntity.class);

        subject.updateBooking(mockedProperty.getId(), bookingToUpdate.getId(), bookingToUpdate);

        verify(bookingRepositoryMock).save(bookingArgumentCaptor.capture());

        final var savedBookingEntity = bookingArgumentCaptor.getValue();

        assertThat(savedBookingEntity.getId()).isEqualTo(bookingToUpdate.getId());
        assertThat(savedBookingEntity.getStartDate()).isEqualTo(bookingToUpdate.getStartDate());
        assertThat(savedBookingEntity.getEndDate()).isEqualTo(bookingToUpdate.getEndDate());
        assertThat(savedBookingEntity.getGuestName()).isEqualTo(bookingToUpdate.getGuestName());
        assertThat(savedBookingEntity.getNumberOfGuests()).isEqualTo(bookingToUpdate.getNumberOfGuests());
        assertThat(savedBookingEntity.getPropertyId()).isEqualTo(bookingToUpdate.getPropertyId());
    }

    @Test
    void shouldUpdateBookingStateToCancelled() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var bookingToUpdate = new Booking(UUID.randomUUID(), at3, at5, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        final var mockedBookingToBeUpdatedEntity = new BookingEntity(bookingToUpdate.getId(), at1, bookingToUpdate.getEndDate(), "Old Guest Name", "2", bookingToUpdate.getPropertyId(), BookingEntityState.CANCELLED);

        mockedProperty.setBookings(List.of(bookingMapper.toBusiness(mockedBookingToBeUpdatedEntity)));

        when(bookingRepositoryMock.findById(bookingToUpdate.getId())).thenReturn(Optional.of(mockedBookingToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var bookingArgumentCaptor = ArgumentCaptor.forClass(BookingEntity.class);

        subject.updateBooking(mockedProperty.getId(), bookingToUpdate.getId(), bookingToUpdate);

        verify(bookingRepositoryMock).save(bookingArgumentCaptor.capture());

        final var savedBookingEntity = bookingArgumentCaptor.getValue();

        assertThat(savedBookingEntity.getId()).isEqualTo(bookingToUpdate.getId());
        assertThat(savedBookingEntity.getStartDate()).isEqualTo(bookingToUpdate.getStartDate());
        assertThat(savedBookingEntity.getEndDate()).isEqualTo(bookingToUpdate.getEndDate());
        assertThat(savedBookingEntity.getGuestName()).isEqualTo(bookingToUpdate.getGuestName());
        assertThat(savedBookingEntity.getNumberOfGuests()).isEqualTo(bookingToUpdate.getNumberOfGuests());
        assertThat(savedBookingEntity.getPropertyId()).isEqualTo(bookingToUpdate.getPropertyId());
    }

    @Test
    void shouldUpdateBookingStateToCancelledEvenWithOverlap() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var bookingToUpdate = new Booking(UUID.randomUUID(), at3, at5, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        final var mockedBookingToBeUpdatedEntity = new BookingEntity(bookingToUpdate.getId(), at1, bookingToUpdate.getEndDate(), "Old Guest Name", "2", bookingToUpdate.getPropertyId(), BookingEntityState.CANCELLED);

        final var otherBooking = new Booking(UUID.randomUUID(), at1, at3, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(bookingMapper.toBusiness(mockedBookingToBeUpdatedEntity), otherBooking));

        when(bookingRepositoryMock.findById(bookingToUpdate.getId())).thenReturn(Optional.of(mockedBookingToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var bookingArgumentCaptor = ArgumentCaptor.forClass(BookingEntity.class);

        subject.updateBooking(mockedProperty.getId(), bookingToUpdate.getId(), bookingToUpdate);

        verify(bookingRepositoryMock).save(bookingArgumentCaptor.capture());

        final var savedBookingEntity = bookingArgumentCaptor.getValue();

        assertThat(savedBookingEntity.getId()).isEqualTo(bookingToUpdate.getId());
        assertThat(savedBookingEntity.getStartDate()).isEqualTo(bookingToUpdate.getStartDate());
        assertThat(savedBookingEntity.getEndDate()).isEqualTo(bookingToUpdate.getEndDate());
        assertThat(savedBookingEntity.getGuestName()).isEqualTo(bookingToUpdate.getGuestName());
        assertThat(savedBookingEntity.getNumberOfGuests()).isEqualTo(bookingToUpdate.getNumberOfGuests());
        assertThat(savedBookingEntity.getPropertyId()).isEqualTo(bookingToUpdate.getPropertyId());
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDateOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(5);
        final var endDate1 = referenceDate.plusDays(1);

        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);


        assertThrows(IllegalArgumentException.class, () ->{
            subject.updateBooking(UUID.randomUUID(), booking.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFoundOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);


        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.updateBooking(UUID.randomUUID(), booking.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenBookingFromDifferentPropertyOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var booking = new Booking(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", UUID.randomUUID(), BookingState.ACTIVE);

        final var bookingEntity = new BookingEntity(booking.getId(), booking.getStartDate(), booking.getEndDate(), booking.getGuestName(), booking.getNumberOfGuests(), booking.getPropertyId(), BookingEntityState.ACTIVE);

        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.of(bookingEntity));

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.updateBooking(propertyId, booking.getId(), booking);
        });

    }

    @Test
    void shouldThrowExceptionWhenBookingIsNotActiveOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var booking = new Booking(UUID.randomUUID(), at3, at5, "Guest name1", "2", propertyId, BookingState.CANCELLED);

        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.of(new BookingEntity(booking.getId(), at3, at5, "Guest name1", "2", propertyId, BookingEntityState.CANCELLED)));

        assertThrows(IllegalArgumentException.class, () ->{
            subject.updateBooking(propertyId, booking.getId(), booking);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBookedOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var bookingToUpdate = new Booking(UUID.randomUUID(), at1, at5, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        final var mockedBookingToBeUpdatedEntity = new BookingEntity(bookingToUpdate.getId(), at3, at5, "Old Guest Name", "2", bookingToUpdate.getPropertyId(), BookingEntityState.ACTIVE);
        final var otherBooking = new Booking(UUID.randomUUID(), at1, at3, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(bookingMapper.toBusiness(mockedBookingToBeUpdatedEntity), otherBooking));

        when(bookingRepositoryMock.findById(bookingToUpdate.getId())).thenReturn(Optional.of(mockedBookingToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBooking(mockedProperty.getId(), bookingToUpdate.getId(), bookingToUpdate);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBlockedOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var bookingToUpdate = new Booking(UUID.randomUUID(), at1, at5, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        final var mockedBookingToBeUpdatedEntity = new BookingEntity(bookingToUpdate.getId(), at3, at5, "Old Guest Name", "2", bookingToUpdate.getPropertyId(), BookingEntityState.ACTIVE);
        final var mockedBlock = new Block(UUID.randomUUID(), at1, at3, mockedProperty.getId(), "Blocked for maintenance");
        mockedProperty.setBookings(List.of(bookingMapper.toBusiness(mockedBookingToBeUpdatedEntity)));
        mockedProperty.setBlocks(List.of(mockedBlock));

        when(bookingRepositoryMock.findById(bookingToUpdate.getId())).thenReturn(Optional.of(mockedBookingToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBooking(mockedProperty.getId(), bookingToUpdate.getId(), bookingToUpdate);
        });
    }

    @Test
    void shouldDeleteBooking() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);


        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBookingToBeDeletedEntity = new BookingEntity(UUID.randomUUID(), at1, at3, "New Guest name", "5", mockedProperty.getId(), BookingEntityState.ACTIVE);

        when(bookingRepositoryMock.findById(mockedBookingToBeDeletedEntity.getId())).thenReturn(Optional.of(mockedBookingToBeDeletedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        subject.deleteBooking(mockedProperty.getId(), mockedBookingToBeDeletedEntity.getId());

        verify(bookingRepositoryMock).deleteById(mockedBookingToBeDeletedEntity.getId());
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFoundOnDelete() {
        final var propertyId = UUID.randomUUID();
        final var bookingId = UUID.randomUUID();

        when(bookingRepositoryMock.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.deleteBooking(propertyId, bookingId);
        });

    }

    @Test
        void shouldThrowExceptionWhenBookingFromDifferentPropertyOnDelete() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);

        final var propertyId = UUID.randomUUID();
        final var mockedBookingToBeDeletedEntity = new BookingEntity(UUID.randomUUID(), at1, at3, "New Guest name", "5", UUID.randomUUID(), BookingEntityState.ACTIVE);

        when(bookingRepositoryMock.findById(mockedBookingToBeDeletedEntity.getId())).thenReturn(Optional.of(mockedBookingToBeDeletedEntity));

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.deleteBooking(propertyId, mockedBookingToBeDeletedEntity.getId());
        });
    }
}
