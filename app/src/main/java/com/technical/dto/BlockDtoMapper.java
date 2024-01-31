package com.technical.dto;

import com.technical.model.Block;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockDtoMapper {

        Block toBusiness(BlockDto blockDto);

        BlockDto toDto(Block block);
}
