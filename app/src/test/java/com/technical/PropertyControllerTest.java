package com.technical;

import com.technical.integration.AbstractIntegrationTest;
import com.technical.model.First;
import com.technical.model.Property;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PropertyControllerTest extends AbstractIntegrationTest {

    @MockBean
    private PropertyService propertyService;

    @Test
    void shouldCreateFirst() throws Exception {

        final var property = new Property(UUID.randomUUID(),"Address line", "City", "Owner full name");
        
        when(propertyService.createProperty(any(Property.class))).thenReturn(property);

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"Address line\",\"city\":\"City\",\"ownerName\":\"Owner full name\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value(property.getAddress()))
                .andExpect(jsonPath("$.city").value(property.getCity()))
                .andExpect(jsonPath("$.ownerName").value(property.getOwnerName()))
                .andExpect(jsonPath("$.id").value(property.getId().toString()));

    }
}
