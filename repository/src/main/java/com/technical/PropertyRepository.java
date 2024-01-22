package com.technical;

import com.technical.entity.PropertyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PropertyRepository extends CrudRepository<PropertyEntity, UUID> {
}
