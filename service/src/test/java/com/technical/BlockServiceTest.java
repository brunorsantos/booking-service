package com.technical;

import com.technical.entity.BlockEntity;
import com.technical.exception.ConflictedDateException;
import com.technical.exception.ResourceNotFoundException;
import com.technical.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BlockServiceTest {

    private BlockService subject;
    private BlockRepository blockRepositoryMock;

    private PropertyService propertyServiceMock;

    private BlockMapper blockMapper;

    @BeforeEach
    void setUp() {
        blockMapper = new BlockMapperImpl();
        blockRepositoryMock = mock(BlockRepository.class);
        propertyServiceMock = mock(PropertyService.class);
        subject = new BlockServiceImpl(blockRepositoryMock, blockMapper, propertyServiceMock);
    }

    @Test
    void shouldGetBlockByPropertyId() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);
        final var startDate2 = referenceDate.plusDays(9);
        final var endDate2 = referenceDate.plusDays(15);

        final var propertyId = UUID.randomUUID();
        final var block1 = new Block(UUID.randomUUID(),startDate1, endDate1, propertyId,"reason1");
        final var block2 = new Block(UUID.randomUUID(),startDate2, endDate2, propertyId,"reason2");

        final var blockEntities = List.of(new BlockEntity(block1.getId(), block1.getStartDate(), block1.getEndDate(), block1.getPropertyId(), block1.getReason()),
                new BlockEntity(block2.getId(), block2.getStartDate(), block2.getEndDate(), block2.getPropertyId(), block2.getReason()));

        when(blockRepositoryMock.findByPropertyId(propertyId)).thenReturn(blockEntities);

        final var blocks = subject.getBlocksByPropertyId(propertyId);

        assertThat(blocks.size()).isEqualTo(2);
        assertThat(blocks.get(0).getId()).isEqualTo(block1.getId());
        assertThat(blocks.get(0).getStartDate()).isEqualTo(block1.getStartDate());
        assertThat(blocks.get(0).getEndDate()).isEqualTo(block1.getEndDate());
        assertThat(blocks.get(0).getPropertyId()).isEqualTo(block1.getPropertyId());
        assertThat(blocks.get(0).getReason()).isEqualTo(block1.getReason());
        assertThat(blocks.get(1).getId()).isEqualTo(block2.getId());
        assertThat(blocks.get(1).getStartDate()).isEqualTo(block2.getStartDate());
        assertThat(blocks.get(1).getEndDate()).isEqualTo(block2.getEndDate());
        assertThat(blocks.get(1).getPropertyId()).isEqualTo(block2.getPropertyId());
        assertThat(blocks.get(1).getReason()).isEqualTo(block2.getReason());
    }

    @Test
    void shouldGetBlock(){
        final var referenceDate = LocalDate.now();
        final var startDate = referenceDate.plusDays(1);
        final var endDate = referenceDate.plusDays(5);
        final var propertyId = UUID.randomUUID();

        final var block = new Block(UUID.randomUUID(),startDate, endDate, propertyId,"reason");

        final var blockEntity = new BlockEntity(block.getId(), block.getStartDate(), block.getEndDate(), block.getPropertyId(), block.getReason());

        when(blockRepositoryMock.findById(block.getId())).thenReturn(java.util.Optional.of(blockEntity));

        final var returnedBlock = subject.getBlock(propertyId, block.getId());

        assertThat(returnedBlock.getId()).isEqualTo(block.getId());
        assertThat(returnedBlock.getStartDate()).isEqualTo(block.getStartDate());
        assertThat(returnedBlock.getEndDate()).isEqualTo(block.getEndDate());
        assertThat(returnedBlock.getPropertyId()).isEqualTo(block.getPropertyId());
        assertThat(returnedBlock.getReason()).isEqualTo(block.getReason());
    }

    @Test
    void shouldThrowExceptionWhenBlockNotFound() {
        final var propertyId = UUID.randomUUID();
        final var blockId = UUID.randomUUID();

        when(blockRepositoryMock.findById(blockId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.getBlock(propertyId, blockId);
        });

    }

    @Test
    void shouldThrowExceptionWhenBlockFromDifferentProperty() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var block = new Block(UUID.randomUUID(),startDate1, endDate1, UUID.randomUUID(),"reason");

        final var blockEntity = new BlockEntity(block.getId(), block.getStartDate(), block.getEndDate(), block.getPropertyId(), block.getReason());

        when(blockRepositoryMock.findById(block.getId())).thenReturn(Optional.of(blockEntity));

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.getBlock(propertyId, block.getId());
        });
    }

    @Test
    void shouldCreateBlock() {
        //Prepare
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBooking = new Booking(UUID.randomUUID(), at1, at3, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(mockedBooking));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        final var block = new Block(UUID.randomUUID(), at3, at5, mockedProperty.getId(), "reason");
        final var bookingArgumentCaptor = ArgumentCaptor.forClass(BlockEntity.class);

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        //Execute
        subject.createBlock(mockedProperty.getId(), block);

        //Assert
        verify(blockRepositoryMock).save(bookingArgumentCaptor.capture());

        final var savedBookingEntity = bookingArgumentCaptor.getValue();

        assertThat(savedBookingEntity.getId()).isEqualTo(block.getId());
        assertThat(savedBookingEntity.getStartDate()).isEqualTo(block.getStartDate());
        assertThat(savedBookingEntity.getEndDate()).isEqualTo(block.getEndDate());
        assertThat(savedBookingEntity.getPropertyId()).isEqualTo(block.getPropertyId());
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(5);
        final var endDate1 = referenceDate.plusDays(1);

        final var property = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var block = new Block(UUID.randomUUID(), startDate1, endDate1, property.getId(), "reason");

        assertThrows(IllegalArgumentException.class, () ->{
            subject.createBlock(property.getId(), block);
        });
    }


    @Test
    void shouldThrowExceptionWhenPropertyIsBooked() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at2 = referenceDate.plusDays(2);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBooking = new Booking(UUID.randomUUID(), at1, at3, "Guest name1", "2", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(mockedBooking));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var block = new Block(UUID.randomUUID(), at2, at5, mockedProperty.getId(), "reason");

        assertThrows(ConflictedDateException.class, () ->{
            subject.createBlock(mockedProperty.getId(), block);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBlocked() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at2 = referenceDate.plusDays(2);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBlock = new Block(UUID.randomUUID(), at1, at3, mockedProperty.getId(), "reason");
        mockedProperty.setBlocks(List.of(mockedBlock));

        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var block = new Block(UUID.randomUUID(), at2, at5, mockedProperty.getId(), "reason");

        assertThrows(ConflictedDateException.class, () ->{
            subject.createBlock(mockedProperty.getId(), block);
        });
    }

    @Test
    void shouldUpdateBlockDatesAndReason() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");

        final var blockToUpdate = new Block(UUID.randomUUID(), at3, at5, mockedProperty.getId(), "reason");

        mockedProperty.setBlocks(List.of(blockToUpdate));
        final var mockedBlockToBeUpdatedEntity = new BlockEntity(blockToUpdate.getId(), at1, blockToUpdate.getEndDate(), blockToUpdate.getPropertyId(), "old reason");


        when(blockRepositoryMock.findById(blockToUpdate.getId())).thenReturn(Optional.of(mockedBlockToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        final var blockEntityArgumentCaptor = ArgumentCaptor.forClass(BlockEntity.class);

        subject.updateBlock(mockedProperty.getId(), blockToUpdate.getId(), blockToUpdate);

        verify(blockRepositoryMock).save(blockEntityArgumentCaptor.capture());

        final var savedBlockEntity = blockEntityArgumentCaptor.getValue();

        assertThat(savedBlockEntity.getId()).isEqualTo(blockToUpdate.getId());
        assertThat(savedBlockEntity.getStartDate()).isEqualTo(blockToUpdate.getStartDate());
        assertThat(savedBlockEntity.getEndDate()).isEqualTo(blockToUpdate.getEndDate());
        assertThat(savedBlockEntity.getPropertyId()).isEqualTo(blockToUpdate.getPropertyId());
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDateOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(5);
        final var endDate1 = referenceDate.plusDays(1);

        final var block = new Block(UUID.randomUUID(), startDate1, endDate1, UUID.randomUUID(), "reason");


        assertThrows(IllegalArgumentException.class, () ->{
            subject.updateBlock(UUID.randomUUID(), block.getId(), block);
        });
    }

    @Test
    void shouldThrowExceptionWhenBlockNotFoundOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var block = new Block(UUID.randomUUID(), startDate1, endDate1, UUID.randomUUID(), "reason");


        when(blockRepositoryMock.findById(block.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.updateBlock(UUID.randomUUID(), block.getId(), block);
        });
    }

    @Test
    void shouldThrowExceptionWhenBlockFromDifferentPropertyOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var startDate1 = referenceDate.plusDays(1);
        final var endDate1 = referenceDate.plusDays(5);

        final var propertyId = UUID.randomUUID();
        final var block = new Block(UUID.randomUUID(), startDate1, endDate1, UUID.randomUUID(), "reason");

        final var blockEntity = new BlockEntity(block.getId(), block.getStartDate(), block.getEndDate(), block.getPropertyId(), block.getReason());

        when(blockRepositoryMock.findById(block.getId())).thenReturn(Optional.of(blockEntity));


        assertThrows(ResourceNotFoundException.class, () ->{
            subject.updateBlock(propertyId, block.getId(), block);
        });

    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBookedOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var blockToUpdate = new Block(UUID.randomUUID(), at1, at5, mockedProperty.getId(), "reason");
        final var mockedBlockToBeUpdatedEntity = new BlockEntity(blockToUpdate.getId(), at3, at5, blockToUpdate.getPropertyId(), "old reason");

        final var otherBooking = new Booking(UUID.randomUUID(), at1, at3, "New Guest name", "5", mockedProperty.getId(), BookingState.ACTIVE);
        mockedProperty.setBookings(List.of(otherBooking));

        when(blockRepositoryMock.findById(blockToUpdate.getId())).thenReturn(Optional.of(mockedBlockToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBlock(mockedProperty.getId(), blockToUpdate.getId(), blockToUpdate);
        });
    }

    @Test
    void shouldThrowExceptionWhenPropertyIsBlockedOnUpdate() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);
        final var at5 = referenceDate.plusDays(5);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var blockToUpdate = new Block(UUID.randomUUID(), at1, at5, mockedProperty.getId(), "reason");
        final var mockedBlockToBeUpdatedEntity = new BlockEntity(blockToUpdate.getId(), at3, at5, blockToUpdate.getPropertyId(), "old reason");

        final var mockedBlock = new Block(UUID.randomUUID(), at1, at3, mockedProperty.getId(), "reason");
        mockedProperty.setBlocks(List.of(mockedBlock));

        when(blockRepositoryMock.findById(blockToUpdate.getId())).thenReturn(Optional.of(mockedBlockToBeUpdatedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);


        assertThrows(ConflictedDateException.class, () ->{
            subject.updateBlock(mockedProperty.getId(), blockToUpdate.getId(), blockToUpdate);
        });
    }

    @Test
    void shouldDeleteBlock() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);

        final var mockedProperty = new Property(UUID.randomUUID(), "Address line", "City", "Robert Johnson");
        final var mockedBlockToBeDeletedEntity = new BlockEntity(UUID.randomUUID(), at1, at3,  mockedProperty.getId(), "reason");

        when(blockRepositoryMock.findById(mockedBlockToBeDeletedEntity.getId())).thenReturn(Optional.of(mockedBlockToBeDeletedEntity));
        when(propertyServiceMock.getPropertyEnriched(mockedProperty.getId())).thenReturn(mockedProperty);

        subject.deleteBlock(mockedProperty.getId(), mockedBlockToBeDeletedEntity.getId());

        verify(blockRepositoryMock).deleteById(mockedBlockToBeDeletedEntity.getId());
    }

    @Test
    void shouldThrowExceptionWhenBlockNotFoundOnDelete() {
        final var propertyId = UUID.randomUUID();
        final var blockId = UUID.randomUUID();

        when(blockRepositoryMock.findById(blockId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.deleteBlock(propertyId, blockId);
        });
    }

    @Test
    void shouldThrowExceptionWhenBlockFromDifferentPropertyOnDelete() {
        final var referenceDate = LocalDate.now();
        final var at1 = referenceDate.plusDays(1);
        final var at3 = referenceDate.plusDays(3);

        final var propertyId = UUID.randomUUID();
        final var mockedBlockToBeDeletedEntity = new BlockEntity(UUID.randomUUID(), at1, at3, UUID.randomUUID(), "reason");

        when(blockRepositoryMock.findById(mockedBlockToBeDeletedEntity.getId())).thenReturn(Optional.of(mockedBlockToBeDeletedEntity));

        assertThrows(ResourceNotFoundException.class, () ->{
            subject.deleteBlock(propertyId, mockedBlockToBeDeletedEntity.getId());
        });
    }

}
