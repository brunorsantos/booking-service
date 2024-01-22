package com.technical.model;

import com.technical.entity.PropertyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    Property toBusiness(PropertyEntity propertyEntity);
    PropertyEntity toEntity(Property property);
}
