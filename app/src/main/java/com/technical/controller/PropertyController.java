package com.technical.controller;


import com.technical.PropertyService;
import com.technical.dto.PropertyDtoMapper;
import com.technical.model.Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.technical.util.Logging.addLoggingContextId;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/properties")
@Slf4j
public class PropertyController {

    PropertyService propertyService;
    PropertyDtoMapper mapper;

    @Autowired
    public PropertyController(final PropertyService propertyService, final PropertyDtoMapper mapper) {
        this.propertyService = propertyService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return new ResponseEntity<>(propertyService.getAllProperties(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(@PathVariable UUID id) {
        addLoggingContextId(id);
        return new ResponseEntity<>(propertyService.getProperty(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        return new ResponseEntity<>(propertyService.createProperty(property), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable UUID id, @RequestBody Property property) {
        addLoggingContextId(id);
        return new ResponseEntity<>(propertyService.updateProperty(id, property), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Property> deleteProperty(@PathVariable UUID id) {
        addLoggingContextId(id);
        propertyService.deleteProperty(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
