package com.technical;

import com.technical.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends CrudRepository<BookingEntity, UUID> {

    List<BookingEntity> findByPropertyId(UUID propertyId);
}
