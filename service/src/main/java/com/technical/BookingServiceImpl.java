package com.technical;

import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Booking;
import com.technical.model.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(final BookingRepository bookingRepository,final BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<Booking> getBookingsByPropertyId(UUID propertyId) {
        final var bookingEntities = bookingRepository.findByPropertyId(propertyId);
        return bookingEntities.stream()
                .map(bookingMapper::toBusiness)
                .collect(Collectors.toList());
    }

    @Override
    public Booking getBooking(UUID propertyId, UUID id) {
        final var bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!bookingEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Booking not found");
        }

        return bookingMapper.toBusiness(bookingEntity);
    }
}
