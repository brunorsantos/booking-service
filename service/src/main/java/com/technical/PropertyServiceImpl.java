package com.technical;

import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Property;
import com.technical.model.PropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public Property createProperty(final Property property) {
        final var propertyEntity = propertyRepository.save(propertyMapper.toEntity(property));
        return propertyMapper.toBusiness(propertyEntity);
    }

    @Override
    public Property getProperty(final UUID id) {
        final var propertyEntity = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        return propertyMapper.toBusiness(propertyEntity);
    }

    @Override
    public Property updateProperty(final UUID id, final Property property) {
        if (propertyRepository.existsById(id)) {
            final var propertyEntity = propertyRepository.save(propertyMapper.toEntity(property));
            return propertyMapper.toBusiness(propertyEntity);
        }
        throw new ResourceNotFoundException("Property not found");
    }

    @Override
    public void deleteProperty(final UUID id) {
        if (propertyRepository.existsById(id)) {
            propertyRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException("Property not found");
    }
}
