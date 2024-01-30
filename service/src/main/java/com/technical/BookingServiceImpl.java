package com.technical;

import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Booking;
import com.technical.model.BookingMapper;
import com.technical.model.BookingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final PropertyService propertyService;

    @Autowired
    public BookingServiceImpl(final BookingRepository bookingRepository, final BookingMapper bookingMapper, final PropertyService propertyService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.propertyService = propertyService;
    }

    @Override
    public List<Booking> getBookingsByPropertyId(final UUID propertyId) {
        final var bookingEntities = bookingRepository.findByPropertyId(propertyId);
        return bookingEntities.stream()
                .map(bookingMapper::toBusiness)
                .collect(Collectors.toList());
    }

    @Override
    public Booking getBooking(final UUID propertyId, final UUID id) {
        final var bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!bookingEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Booking not found");
        }

        return bookingMapper.toBusiness(bookingEntity);
    }

    @Override
    public Booking createBooking(final UUID propertyId, final Booking booking) {

        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (booking.getBookingState() != BookingState.ACTIVE) {
            throw new IllegalArgumentException("New booking must be in ACTIVE state");
        }

        final var property = propertyService.getPropertyWithBookings(propertyId);

        if (property.isBooked(booking.getStartDate(), booking.getEndDate())) {
            throw new ConflictedDateException("Property is already booked for the selected dates");
        }

        final var bookingEntity = bookingMapper.toEntity(booking);
        bookingEntity.setPropertyId(propertyId);
        final var savedBookingEntity = bookingRepository.save(bookingEntity);
        return bookingMapper.toBusiness(savedBookingEntity);
    }

    @Override
    public Booking updateBooking(final UUID propertyId, final UUID id, final Booking booking) {

        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        final var returnedBookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        final var returnedBooking = bookingMapper.toBusiness(returnedBookingEntity);

        if (!returnedBooking.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Booking not found");
        }


        if (booking.getBookingState() == BookingState.CANCELLED && returnedBooking.getBookingState() != BookingState.ACTIVE) {
            throw new IllegalArgumentException("Only Active bookings can be cancelled");
        }

        final var property = propertyService.getPropertyWithBookings(propertyId);

        if (booking.getBookingState() == BookingState.ACTIVE && property.isBooked(booking.getStartDate(), booking.getEndDate(), id)) {
            throw new ConflictedDateException("Property is already booked for the selected dates");
        }

        booking.setId(id);
        booking.setPropertyId(propertyId);
        final var savedBookingEntity = bookingRepository.save(bookingMapper.toEntity(booking));
        return bookingMapper.toBusiness(savedBookingEntity);
    }

    @Override
    public void deleteBooking(final UUID propertyId, final UUID id) {
        final var bookingEntity = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!bookingEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Booking not found");
        }

        bookingRepository.deleteById(id);
    }
}
