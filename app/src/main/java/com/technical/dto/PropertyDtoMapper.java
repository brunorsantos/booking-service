package com.technical.dto;

import com.technical.model.Property;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyDtoMapper {

        Property toBusiness(PropertyDto propertyDto);

        PropertyDto toDto(Property property);
}
