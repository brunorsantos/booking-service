package com.technical;

import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Property;
import com.technical.model.PropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    private final BookingService bookingService;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyMapper propertyMapper, BookingService bookingService){
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.bookingService = bookingService;
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
    public Property getPropertyWithBookings(UUID id) {
        final var propertyEntity = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        var property =  propertyMapper.toBusiness(propertyEntity);

        final var bookings = bookingService.getBookingsByPropertyId(id);
        property.setBookings(bookings);

        return property;
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
        //TODO: check bookings and blocks before deleting
        if (propertyRepository.existsById(id)) {
            propertyRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException("Property not found");
    }

    @Override
    public List<Property> getAllProperties() {
        return StreamSupport.stream(propertyRepository.findAll().spliterator(), false)
                .map(propertyMapper::toBusiness)
                .collect(Collectors.toList());
    }
}
