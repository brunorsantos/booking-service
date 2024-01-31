package com.technical.controller;

import com.technical.BlockService;
import com.technical.dto.BlockDto;
import com.technical.dto.BlockDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.technical.util.Logging.addLoggingContextId;

@RestController
@RequestMapping("/properties/{propertyId}/blocks")
@Slf4j
public class BlockController {

    BlockService blockService;

    BlockDtoMapper mapper;

    @Autowired
    public BlockController(BlockService blockService, BlockDtoMapper mapper) {
        this.blockService = blockService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BlockDto>> getAllBlocks(@PathVariable UUID propertyId) {
        addLoggingContextId();
        final var blocks = blockService.getBlocksByPropertyId(propertyId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(blocks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockDto> getBlock(@PathVariable UUID propertyId, @PathVariable UUID id) {
        addLoggingContextId();
        return new ResponseEntity<>(mapper.toDto(blockService.getBlock(propertyId, id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BlockDto> createBlock(@PathVariable UUID propertyId, @Valid @RequestBody BlockDto block) {
        addLoggingContextId();
        return new ResponseEntity<>(mapper.toDto(blockService.createBlock(propertyId, mapper.toBusiness(block))), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockDto> updateBlock(@PathVariable UUID propertyId, @PathVariable UUID id, @Valid @RequestBody BlockDto block) {
        addLoggingContextId();
        return new ResponseEntity<>(mapper.toDto(blockService.updateBlock(propertyId, id, mapper.toBusiness(block))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable UUID propertyId, @PathVariable UUID id) {
        blockService.deleteBlock(propertyId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
