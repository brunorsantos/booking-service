package com.technical;

import com.technical.entity.BookingEntity;
import com.technical.entity.BookingEntityState;
import com.technical.entity.PropertyEntity;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PropertyServiceTest {

    private PropertyServiceImpl subject;

    private PropertyRepository propertyRepositoryMock;

    private BookingRepository bookingRepositoryMock;

    @BeforeEach
    void setUp() {
        var mapper = new PropertyMapperImpl();
        var bookingMapper = new BookingMapperImpl();
        propertyRepositoryMock = mock(PropertyRepository.class);
        bookingRepositoryMock = mock(BookingRepository.class);
        subject = new PropertyServiceImpl(propertyRepositoryMock, mapper, bookingRepositoryMock, bookingMapper);
    }

    @Test
    void shouldCreateProperty() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");
        final var propertyArgumentCaptor = ArgumentCaptor.forClass(PropertyEntity.class);
        when(propertyRepositoryMock.save(any())).thenReturn(new PropertyEntity(property.getId(), property.getAddress(), property.getCity(), property.getOwnerName()));

        // Execute
        final var propertyCreated = subject.createProperty(property);

        // Assert
        verify(propertyRepositoryMock).save(propertyArgumentCaptor.capture());

        final var propertyEntity = propertyArgumentCaptor.getValue();

        assertThat(propertyEntity.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyCreated.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyEntity.getCity()).isEqualTo(property.getCity());
        assertThat(propertyCreated.getCity()).isEqualTo(property.getCity());
        assertThat(propertyEntity.getOwnerName()).isEqualTo(property.getOwnerName());
        assertThat(propertyCreated.getOwnerName()).isEqualTo(property.getOwnerName());
    }

    @Test
    void shouldGetProperty() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");

        when(propertyRepositoryMock.findById(any())).thenReturn(java.util.Optional.of(new PropertyEntity(property.getId(), property.getAddress(), property.getCity(), property.getOwnerName())));

        // Execute
        final var propertyRetrieved = subject.getProperty(property.getId());

        assertThat(propertyRetrieved.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyRetrieved.getCity()).isEqualTo(property.getCity());
        assertThat(propertyRetrieved.getOwnerName()).isEqualTo(property.getOwnerName());
        assertThat(propertyRetrieved.getId()).isEqualTo(property.getId());
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFound() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");

        when(propertyRepositoryMock.findById(any())).thenReturn(java.util.Optional.empty());

        // Execute
        try {
            subject.getProperty(property.getId());
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Property not found");
        }
    }

    @Test
    void shouldUpdateProperty() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");
        final var propertyArgumentCaptor = ArgumentCaptor.forClass(PropertyEntity.class);
        when(propertyRepositoryMock.existsById(any())).thenReturn(true);
        when(propertyRepositoryMock.save(any())).thenReturn(new PropertyEntity(property.getId(), property.getAddress(), property.getCity(), property.getOwnerName()));

        // Execute
        final var propertyUpdated = subject.updateProperty(property.getId(), property);

        // Assert
        verify(propertyRepositoryMock).save(propertyArgumentCaptor.capture());

        final var propertyEntity = propertyArgumentCaptor.getValue();

        assertThat(propertyEntity.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyUpdated.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyEntity.getCity()).isEqualTo(property.getCity());
        assertThat(propertyUpdated.getCity()).isEqualTo(property.getCity());
        assertThat(propertyEntity.getOwnerName()).isEqualTo(property.getOwnerName());
        assertThat(propertyUpdated.getOwnerName()).isEqualTo(property.getOwnerName());
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFoundOnUpdate() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");

        when(propertyRepositoryMock.existsById(any())).thenReturn(false);

        // Execute
        try {
            subject.updateProperty(property.getId(), property);
        } catch (ResourceNotFoundException e) {
            verify(propertyRepositoryMock, never()).save(any());
            assertThat(e.getMessage()).isEqualTo("Property not found");
        }
    }

    @Test
    void shouldDeleteProperty() {
        // Prepare
        final var propertyId = UUID.randomUUID();
        when(propertyRepositoryMock.existsById(any())).thenReturn(true);

        // Execute
        subject.deleteProperty(propertyId);

        // Assert
        verify(propertyRepositoryMock).deleteById(propertyId);
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFoundOnDelete() {
        // Prepare
        final var propertyId = UUID.randomUUID();

        when(propertyRepositoryMock.existsById(any())).thenReturn(false);

        // Execute
        try {
            subject.deleteProperty(propertyId);
        } catch (ResourceNotFoundException e) {
            verify(propertyRepositoryMock, never()).deleteById(propertyId);
            assertThat(e.getMessage()).isEqualTo("Property not found");
        }
    }

    @Test
    void shouldGetAllProperties() {
        // Prepare
        final var property1 = new Property(UUID.randomUUID(),"Address line1", "City", "Owner full name");
        final var property2 = new Property(UUID.randomUUID(),"Address line2", "City", "Owner full name");
        final var property3 = new Property(UUID.randomUUID(),"Address line3", "City", "Owner full name");

        when(propertyRepositoryMock.findAll()).thenReturn(java.util.List.of(new PropertyEntity(property1.getId(), property1.getAddress(), property1.getCity(), property1.getOwnerName()),
                new PropertyEntity(property2.getId(), property2.getAddress(), property2.getCity(), property2.getOwnerName()),
                new PropertyEntity(property3.getId(), property3.getAddress(), property3.getCity(), property3.getOwnerName())));

        // Execute
        final var properties = subject.getAllProperties();

        assertThat(properties.size()).isEqualTo(3);
        assertThat(properties.get(0).getAddress()).isEqualTo(property1.getAddress());
        assertThat(properties.get(0).getCity()).isEqualTo(property1.getCity());
        assertThat(properties.get(0).getOwnerName()).isEqualTo(property1.getOwnerName());
        assertThat(properties.get(0).getId()).isEqualTo(property1.getId());
        assertThat(properties.get(1).getAddress()).isEqualTo(property2.getAddress());
        assertThat(properties.get(1).getCity()).isEqualTo(property2.getCity());
        assertThat(properties.get(1).getOwnerName()).isEqualTo(property2.getOwnerName());
        assertThat(properties.get(1).getId()).isEqualTo(property2.getId());
        assertThat(properties.get(2).getAddress()).isEqualTo(property3.getAddress());
        assertThat(properties.get(2).getCity()).isEqualTo(property3.getCity());
        assertThat(properties.get(2).getOwnerName()).isEqualTo(property3.getOwnerName());
        assertThat(properties.get(2).getId()).isEqualTo(property3.getId());
    }

    @Test
    void shouldGetPropertyWithBookings() {
        // Prepare
        final var referenceDate = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);

        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Owner full name");
        final var booking1 = new BookingEntity(UUID.randomUUID(), startDate1, endDate1, "Guest name1", "2", property.getId(), BookingEntityState.ACTIVE);
        final var booking2 = new BookingEntity(UUID.randomUUID(), startDate2, endDate2, "Guest name2", "5", property.getId(), BookingEntityState.ACTIVE);

        when(propertyRepositoryMock.findById(any()))
                .thenReturn(java.util.Optional.of(new PropertyEntity(property.getId(), property.getAddress(), property.getCity(), property.getOwnerName())));

        when(bookingRepositoryMock.findByPropertyId(any())).thenReturn(java.util.List.of(booking1, booking2));

        // Execute
        final var propertyRetrieved = subject.getPropertyWithBookings(property.getId());

        // Assert
        assertThat(propertyRetrieved.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyRetrieved.getCity()).isEqualTo(property.getCity());
        assertThat(propertyRetrieved.getOwnerName()).isEqualTo(property.getOwnerName());
        assertThat(propertyRetrieved.getId()).isEqualTo(property.getId());
        assertThat(propertyRetrieved.getBookings().size()).isEqualTo(2);
        assertThat(propertyRetrieved.getBookings().get(0).getId()).isEqualTo(booking1.getId());
        assertThat(propertyRetrieved.getBookings().get(0).getStartDate()).isEqualTo(booking1.getStartDate());
        assertThat(propertyRetrieved.getBookings().get(0).getEndDate()).isEqualTo(booking1.getEndDate());
        assertThat(propertyRetrieved.getBookings().get(0).getGuestName()).isEqualTo(booking1.getGuestName());
        assertThat(propertyRetrieved.getBookings().get(0).getNumberOfGuests()).isEqualTo(booking1.getNumberOfGuests());

    }

    @Test
    void shouldThrowExceptionWhenPropertyWhithBookingsNotFound() {
        // Prepare
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");

        when(propertyRepositoryMock.findById(any())).thenReturn(java.util.Optional.empty());

        // Execute
        try {
            subject.getPropertyWithBookings(property.getId());
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Property not found");
        }
    }
}
