package com.technical;

import com.technical.model.Block;

import java.util.List;
import java.util.UUID;

public interface BlockService {
    List<Block> getBlocksByPropertyId(UUID propertyId);

    Block getBlock(UUID propertyId, UUID id);

    Block createBlock(UUID propertyId, Block booking);

    Block updateBlock(UUID propertyId, UUID id, Block booking);

    void deleteBlock(UUID propertyId, UUID id);

}
