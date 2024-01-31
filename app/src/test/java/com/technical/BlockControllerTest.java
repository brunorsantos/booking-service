package com.technical;

import com.technical.BlockService;
import com.technical.integration.AbstractIntegrationTest;
import com.technical.model.Block;

import com.technical.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BlockControllerTest extends AbstractIntegrationTest {

    @MockBean
    private BlockService blockService;

    @Test
    void shouldGetAllBlocks() throws Exception{

        final var propertyId = UUID.randomUUID();

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);

        final var block1 = new Block(UUID.randomUUID(), startDate1, endDate1, propertyId, "reason1");
        final var block2 = new Block(UUID.randomUUID(), startDate2, endDate2, propertyId, "reason2");

        when(blockService.getBlocksByPropertyId(propertyId)).thenReturn(List.of(block1, block2));

        mockMvc.perform(get("/properties/" + propertyId + "/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startDate").value(startDate1.toString()))
                .andExpect(jsonPath("$[0].endDate").value(endDate1.toString()))
                .andExpect(jsonPath("$[0].propertyId").value(propertyId.toString()))
                .andExpect(jsonPath("$[0].reason").value("reason1"))
                .andExpect(jsonPath("$[1].startDate").value(startDate2.toString()))
                .andExpect(jsonPath("$[1].endDate").value(endDate2.toString()))
                .andExpect(jsonPath("$[1].propertyId").value(propertyId.toString()))
                .andExpect(jsonPath("$[1].reason").value("reason2"));
    }

    @Test
    void shouldGetBooking() throws Exception {
        final var blockId = UUID.randomUUID();
        final var propertyId = UUID.randomUUID();
        final var startDate = LocalDate.now().plusDays(1);
        final var endDate = LocalDate.now().plusDays(5);
        final var block = new Block(blockId, startDate, endDate, propertyId, "reason");

        when(blockService.getBlock(propertyId, blockId)).thenReturn(block);

        mockMvc.perform(get("/properties/" + propertyId + "/blocks/" + blockId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.propertyId").value(propertyId.toString()))
                .andExpect(jsonPath("$.reason").value("reason"));
    }

    @Test
    void shouldCreateBlock() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var startDate = LocalDate.now().plusDays(1);
        final var endDate = LocalDate.now().plusDays(5);
        final var block = new Block(UUID.randomUUID(), startDate, endDate, propertyId, "reason");

        when(blockService.createBlock(any(UUID.class), any(Block.class))).thenReturn(block);

        mockMvc.perform(post("/properties/" + propertyId + "/blocks")
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\", \"reason\": \"reason\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.propertyId").value(propertyId.toString()))
                .andExpect(jsonPath("$.reason").value("reason"));
    }

    @Test
    void shouldUpdateBlock() throws Exception {
        final var blockId = UUID.randomUUID();
        final var propertyId = UUID.randomUUID();
        final var startDate = LocalDate.now().plusDays(1);
        final var endDate = LocalDate.now().plusDays(5);
        final var block = new Block(blockId, startDate, endDate, propertyId, "reason");

        when(blockService.updateBlock(any(UUID.class), any(UUID.class), any(Block.class))).thenReturn(block);

        mockMvc.perform(put("/properties/" + propertyId + "/blocks/" + blockId)
                .contentType("application/json")
                .content("{\"startDate\": \"" + startDate + "\", \"endDate\": \"" + endDate + "\", \"reason\": \"reason\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.propertyId").value(propertyId.toString()))
                .andExpect(jsonPath("$.reason").value("reason"));
    }

    @Test
    void shouldDeleteBooking() throws Exception {

        final var propertyId = UUID.randomUUID();
        final var blockId = UUID.randomUUID();

        mockMvc.perform(delete("/properties/" + propertyId + "/blocks/" + blockId))
                .andExpect(status().isNoContent());
    }
}
