package com.technical.dto;

import com.technical.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingDtoMapper {

        Booking toBusiness(BookingDto bookingDto);

        BookingDto toDto(Booking booking);
}
