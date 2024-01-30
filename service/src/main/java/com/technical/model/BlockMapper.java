package com.technical.model;

import com.technical.entity.BlockEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    Block toBusiness(BlockEntity blockEntity);
    BlockEntity toEntity(Block block);
}
