package com.technical;

import com.technical.exception.ResourceNotFoundException;
import com.technical.integration.AbstractIntegrationTest;
import com.technical.model.Property;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PropertyControllerTest extends AbstractIntegrationTest {

    @MockBean
    private PropertyService propertyService;

    @Test
    void shouldCreateProperty() throws Exception {

        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Al green");

        when(propertyService.createProperty(any(Property.class))).thenReturn(property);

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Address line\",\"city\":\"City\",\"ownerName\":\"Al green\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value(property.getAddress()))
                .andExpect(jsonPath("$.city").value(property.getCity()))
                .andExpect(jsonPath("$.ownerName").value(property.getOwnerName()))
                .andExpect(jsonPath("$.id").value(property.getId().toString()));

    }

    @Test
    void shouldGetProperty() throws Exception {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Wilson Pickett");

        when(propertyService.getProperty(any(UUID.class))).thenReturn(property);

        mockMvc.perform(get("/properties/" + property.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(property.getAddress()))
                .andExpect(jsonPath("$.city").value(property.getCity()))
                .andExpect(jsonPath("$.ownerName").value(property.getOwnerName()))
                .andExpect(jsonPath("$.id").value(property.getId().toString()));
    }

    @Test
    void shouldUpdateProperty() throws Exception {
        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Otis Redding");

        when(propertyService.updateProperty(any(UUID.class), any(Property.class))).thenReturn(property);

        mockMvc.perform(put("/properties/" + property.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Address line\",\"city\":\"City\",\"ownerName\":\"Otis Redding\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(property.getAddress()))
                .andExpect(jsonPath("$.city").value(property.getCity()))
                .andExpect(jsonPath("$.ownerName").value(property.getOwnerName()))
                .andExpect(jsonPath("$.id").value(property.getId().toString()));
    }

    @Test
    void shouldDeleteProperty() throws Exception {

        final var propertyId = UUID.randomUUID();

        mockMvc.perform(delete("/properties/" + propertyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(propertyService).deleteProperty(propertyId);

    }

    @Test
    void shouldGetAllProperties() throws Exception {
        final var property1 = new Property(UUID.randomUUID(),"Address line", "City", "Isaac Hayes");
        final var property2 = new Property(UUID.randomUUID(),"Address line", "City", "Hubert Sumlin");

        when(propertyService.getAllProperties()).thenReturn(List.of(property1, property2));

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value(property1.getAddress()))
                .andExpect(jsonPath("$[0].city").value(property1.getCity()))
                .andExpect(jsonPath("$[0].ownerName").value(property1.getOwnerName()))
                .andExpect(jsonPath("$[0].id").value(property1.getId().toString()))
                .andExpect(jsonPath("$[1].address").value(property2.getAddress()))
                .andExpect(jsonPath("$[1].city").value(property2.getCity()))
                .andExpect(jsonPath("$[1].ownerName").value(property2.getOwnerName()))
                .andExpect(jsonPath("$[1].id").value(property2.getId().toString()));
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFoundOnGet() throws Exception {
        final var propertyId = UUID.randomUUID();

        when(propertyService.getProperty(any(UUID.class))).thenThrow(new ResourceNotFoundException("Property not found"));

        mockMvc.perform(get("/properties/" + propertyId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Property not found"));
    }

}
