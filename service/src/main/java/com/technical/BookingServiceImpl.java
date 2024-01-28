package com.technical;


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
}
