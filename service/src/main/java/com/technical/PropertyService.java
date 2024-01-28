package com.technical;

import com.technical.model.Property;

import java.util.List;
import java.util.UUID;

public interface PropertyService {
    Property createProperty(Property property);
    Property getProperty(UUID id);
    Property getPropertyWithBookings(UUID id);
    Property updateProperty(UUID id, Property property);
    void deleteProperty(UUID id);
    List<Property> getAllProperties();
}
