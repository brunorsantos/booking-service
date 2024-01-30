package com.technical;

import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.Block;
import com.technical.model.BlockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;
    private final PropertyService propertyService;

    @Autowired
    public BlockServiceImpl(final BlockRepository blockRepository, final BlockMapper blockMapper, final PropertyService propertyService) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
        this.propertyService = propertyService;
    }

    @Override
    public List<Block> getBlocksByPropertyId(final UUID propertyId) {
        final var blockEntities = blockRepository.findByPropertyId(propertyId);
        return blockEntities.stream()
                .map(blockMapper::toBusiness)
                .collect(Collectors.toList());
    }

    @Override
    public Block getBlock(final UUID propertyId, final UUID id) {
        final var blockEntity = blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Block not found"));

        if (!blockEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Block not found");
        }

        return blockMapper.toBusiness(blockEntity);
    }

    @Override
    public Block createBlock(final UUID propertyId, final Block block) {

        if (block.getStartDate().isAfter(block.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        final var property = propertyService.getPropertyWithBookings(propertyId);

        if (property.isBooked(block.getStartDate(), block.getEndDate())) {
            throw new ConflictedDateException("Property is already booked for the selected dates");
        }

        block.setPropertyId(propertyId);
        final var savedBlock = blockRepository.save(blockMapper.toEntity(block));
        return blockMapper.toBusiness(savedBlock);
    }

    @Override
    public Block updateBlock(final UUID propertyId, final UUID id, final Block block) {

        if (block.getStartDate().isAfter(block.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        final var returnedBlockEntity = blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Block not found"));

        if (!returnedBlockEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Block not found");
        }

        final var property = propertyService.getPropertyWithBookings(propertyId);

        if (property.isBooked(block.getStartDate(), block.getEndDate())) {
            throw new ConflictedDateException("Property is already booked for the selected dates");
        }

        block.setId(id);
        block.setPropertyId(propertyId);
        final var savedBlock = blockRepository.save(blockMapper.toEntity(block));
        return blockMapper.toBusiness(savedBlock);
    }

    @Override
    public void deleteBlock(final UUID propertyId, final UUID id) {
        final var blockEntity = blockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Block not found"));

        if (!blockEntity.getPropertyId().equals(propertyId)) {
            throw new ResourceNotFoundException("Block not found");
        }

        blockRepository.deleteById(id);
    }
}
