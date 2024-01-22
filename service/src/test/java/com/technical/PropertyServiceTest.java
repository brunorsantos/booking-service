package com.technical;

import com.technical.entity.PropertyEntity;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PropertyServiceTest {

    private PropertyServiceImpl subject;

    private PropertyRepository propertyRepositoryMock;

    @BeforeEach
    void setUp() {
        var mapper = new PropertyMapperImpl();
        propertyRepositoryMock = mock(PropertyRepository.class);
        subject = new PropertyServiceImpl(propertyRepositoryMock, mapper);
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


}
