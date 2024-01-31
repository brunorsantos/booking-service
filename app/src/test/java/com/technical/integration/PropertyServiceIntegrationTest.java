package com.technical.integration;


import com.technical.PropertyRepository;
import com.technical.PropertyServiceImpl;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PropertyServiceIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private PropertyServiceImpl subject;

    @Autowired
    public PropertyServiceIntegrationTest(final PropertyRepository propertyRepository) {
        subject = new PropertyServiceImpl(propertyRepository, propertyMapper, bookingRepository, bookingMapper, blockRepository, blockMapper);
    }

    @BeforeEach
    void setUp() {

        propertyRepository.deleteAll();
    }

    @Test
    void shouldCreateProperty() {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Albert King");

        subject.createProperty(property);

        final var propertyEntities = propertyRepository.findAll();

        final var propertyEntity = propertyEntities.iterator().next();

        assertThat(propertyEntity.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyEntity.getCity()).isEqualTo(property.getCity());
        assertThat(propertyEntity.getOwnerName()).isEqualTo(property.getOwnerName());

    }

    @Test
    void shouldGetProperty() {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Robert Johnson");

        final var propertyEntity = propertyRepository.save(propertyMapper.toEntity(property));

        final var propertyBusiness = subject.getPropertyEnriched(propertyEntity.getId());

        assertThat(propertyBusiness.getAddress()).isEqualTo(propertyEntity.getAddress());
        assertThat(propertyBusiness.getCity()).isEqualTo(propertyEntity.getCity());
        assertThat(propertyBusiness.getOwnerName()).isEqualTo(propertyEntity.getOwnerName());
    }

    @Test
    void shouldGetPropertyNotFound() {
        assertThatThrownBy(() -> subject.getProperty(UUID.randomUUID())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldGetAllProperties() {
        final var property1 = new Property(UUID.randomUUID(),"Address line", "City", "Albert King");
        final var property2 = new Property(UUID.randomUUID(),"Address line", "City", "Robert Johnson");

        propertyRepository.save(propertyMapper.toEntity(property1));
        propertyRepository.save(propertyMapper.toEntity(property2));

        final var properties = subject.getAllProperties();

        assertThat(properties).hasSize(2);
    }

    @Test
    void shouldUpdateProperty() {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Albert King");

        final var propertyEntity = propertyRepository.save(propertyMapper.toEntity(property));

        final var propertyToUpdate = new Property(propertyEntity.getId(),"New Address line", "New City", "Robert Johnson");

        subject.updateProperty(propertyEntity.getId(), propertyToUpdate);

        final var propertyEntities = propertyRepository.findAll();

        final var propertyEntityUpdated = propertyEntities.iterator().next();

        assertThat(propertyEntityUpdated.getAddress()).isEqualTo(propertyToUpdate.getAddress());
        assertThat(propertyEntityUpdated.getCity()).isEqualTo(propertyToUpdate.getCity());
        assertThat(propertyEntityUpdated.getOwnerName()).isEqualTo(propertyToUpdate.getOwnerName());
    }

    @Test
    void shouldDeleteProperty() {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Albert King");

        final var propertyEntity = propertyRepository.save(propertyMapper.toEntity(property));

        subject.deleteProperty(propertyEntity.getId());

        final var propertyEntities = propertyRepository.findAll();

        assertThat(propertyEntities).isEmpty();
    }

}
