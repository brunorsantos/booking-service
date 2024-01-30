package com.technical;

import com.technical.entity.BlockEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BlockRepository extends CrudRepository<BlockEntity, UUID> {

    List<BlockEntity> findByPropertyId(UUID propertyId);
}
