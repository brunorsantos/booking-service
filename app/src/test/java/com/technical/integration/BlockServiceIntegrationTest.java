package com.technical.integration;

import com.technical.*;

import com.technical.entity.BlockEntity;
import com.technical.entity.BookingEntity;
import com.technical.entity.BookingEntityState;
import com.technical.exception.ConflictedDateException;
import com.technical.model.Block;
import com.technical.model.Booking;
import com.technical.model.BookingState;
import com.technical.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BlockServiceIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private BlockServiceImpl subject;

    @Autowired
    public BlockServiceIntegrationTest(final BlockRepository blockRepository, final PropertyService propertyService) {
        subject = new BlockServiceImpl(blockRepository, blockMapper, propertyService);
    }

    @BeforeEach
    void setUp() {
        blockRepository.deleteAll();
        propertyRepository.deleteAll();
    }

    @Test
    void shouldGetBookingsByPropertyId() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);


        final var block1 = new Block(UUID.randomUUID(), startDate1, endDate1, property.getId(), "Reason1");
        final var block2 = new Block(UUID.randomUUID(), startDate2, endDate2, property.getId(), "Reason2");

        final var blockEntities = List.of(new BlockEntity(block1.getId(), block1.getStartDate(), block1.getEndDate(), block1.getPropertyId(), block1.getReason()),
                new BlockEntity(block2.getId(), block2.getStartDate(), block2.getEndDate(), block2.getPropertyId(), block2.getReason()));

        blockRepository.saveAll(blockEntities);

        final var blocks = subject.getBlocksByPropertyId(property.getId());

        assertThat(blocks.size()).isEqualTo(2);
        assertThat(blocks.get(0).getStartDate()).isEqualTo(block1.getStartDate());
        assertThat(blocks.get(0).getEndDate()).isEqualTo(block1.getEndDate());
        assertThat(blocks.get(0).getReason()).isEqualTo(block1.getReason());
    }

    @Test
    void shouldCreateBlock() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var block = new Block(UUID.randomUUID(), startDate, endDate, savedProperty.getId(), "Reason1");

        subject.createBlock(savedProperty.getId(), block);

        final var blocks = subject.getBlocksByPropertyId(savedProperty.getId());

        assertThat(blocks.size()).isEqualTo(1);
        assertThat(blocks.get(0).getStartDate()).isEqualTo(block.getStartDate());
        assertThat(blocks.get(0).getEndDate()).isEqualTo(block.getEndDate());
    }

    @Test
    void shouldUpdateBlock() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var block = new Block(UUID.randomUUID(), startDate, endDate, savedProperty.getId(), "Reason1");
        final var savedBlock = blockRepository.save(blockMapper.toEntity(block));

        final var updatedStartDate = referenceDate.plusDays(2);
        final var updatedEndDate = referenceDate.plusDays(6);

        final var updatedBlock = new Block(savedBlock.getId(), updatedStartDate, updatedEndDate, savedProperty.getId(), "Reason2");

        subject.updateBlock(savedProperty.getId(), savedBlock.getId(), updatedBlock);

        final var blocks = subject.getBlocksByPropertyId(savedProperty.getId());

        assertThat(blocks.size()).isEqualTo(1);
        assertThat(blocks.get(0).getStartDate()).isEqualTo(updatedBlock.getStartDate());
        assertThat(blocks.get(0).getEndDate()).isEqualTo(updatedBlock.getEndDate());
        assertThat(blocks.get(0).getReason()).isEqualTo(updatedBlock.getReason());
    }

    @Test
    void shouldNotUpdateBlockOverlappingWithBooking() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var block = new Block(UUID.randomUUID(), at1, at3, savedProperty.getId(), "Reason1");
        final var savedBooking = blockRepository.save(blockMapper.toEntity(block));

        final var otherBooking = new Booking(UUID.randomUUID(), at3, at5, "Guest name1", "2", savedProperty.getId(), BookingState.ACTIVE);
        bookingRepository.save(bookingMapper.toEntity(otherBooking));


        final var updatedBlock = new Block(savedBooking.getId(), at1, at5, savedProperty.getId(), "Reason1");

        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBlock(savedProperty.getId(), savedBooking.getId(), updatedBlock);
        });
    }

    @Test
    void shouldDeleteBooking() {
        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var savedProperty = propertyRepository.save(propertyMapper.toEntity(property));

        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);

        final var block = new Block(UUID.randomUUID(), startDate, endDate, savedProperty.getId(), "Reason1");
        final var savedBlock = blockRepository.save(blockMapper.toEntity(block));

        subject.deleteBlock(savedProperty.getId(), savedBlock.getId());

        final var blocks = subject.getBlocksByPropertyId(savedProperty.getId());

        assertThat(blocks.size()).isEqualTo(0);
    }
}
