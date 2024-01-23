package com.technical.integration;


import com.technical.PropertyRepository;
import com.technical.PropertyServiceImpl;
import com.technical.model.Property;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyServiceIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private PropertyServiceImpl subject;

    @Autowired
    public PropertyServiceIntegrationTest(final PropertyRepository propertyRepository) {
        subject = new PropertyServiceImpl(propertyRepository, propertyMapper);
    }

    @Test
    void shouldCreateProperty() {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");

        subject.createProperty(property);

        final var propertyEntities = propertyRepository.findAll();

        final var propertyEntity = propertyEntities.iterator().next();

        assertThat(propertyEntity.getAddress()).isEqualTo(property.getAddress());
        assertThat(propertyEntity.getCity()).isEqualTo(property.getCity());
        assertThat(propertyEntity.getOwnerName()).isEqualTo(property.getOwnerName());

    }

}
