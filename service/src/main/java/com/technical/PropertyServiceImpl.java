package com.technical;

import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.BlockMapper;
import com.technical.model.BookingMapper;
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

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               PropertyMapper propertyMapper,
                               BookingRepository bookingRepository,
                                 BookingMapper bookingMapper,
                                 BlockRepository blockRepository,
                                    BlockMapper blockMapper
                               ){
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
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
    public Property getPropertyEnriched(UUID id) {

        var property = getProperty(id);

        final var bookingEntities = bookingRepository.findByPropertyId(id);
        final var bookings = bookingEntities.stream()
                .map(bookingMapper::toBusiness)
                .collect(Collectors.toList());
        property.setBookings(bookings);

        final var blockEntities = blockRepository.findByPropertyId(id);
        final var blocks = blockEntities.stream()
                .map(blockMapper::toBusiness)
                .collect(Collectors.toList());
        property.setBlocks(blocks);

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

        var property = getPropertyEnriched(id);

        if (!property.getBookings().isEmpty()) {
            throw new ConflictedDateException("Property has bookings and cannot be deleted");
        }

        if (!property.getBlocks().isEmpty()) {
            throw new ConflictedDateException("Property has blocks and cannot be deleted");
        }

        propertyRepository.deleteById(id);
    }

    @Override
    public List<Property> getAllProperties() {
        return StreamSupport.stream(propertyRepository.findAll().spliterator(), false)
                .map(propertyMapper::toBusiness)
                .collect(Collectors.toList());
    }
}
