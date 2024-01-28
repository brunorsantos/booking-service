package com.technical.model;

import com.technical.entity.BookingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBusiness(BookingEntity bookingEntity);
    BookingEntity toEntity(Booking booking);
}
