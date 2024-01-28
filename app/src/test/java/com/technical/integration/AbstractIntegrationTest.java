package com.technical.integration;

import com.technical.BookingRepository;
import com.technical.BookingService;
import com.technical.FirstRepository;
import com.technical.PropertyRepository;
import com.technical.dto.FirstDtoMapper;
import com.technical.dto.PropertyDtoMapper;
import com.technical.model.BookingMapper;
import com.technical.model.FirstMapper;
import com.technical.model.PropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PropertyMapper propertyMapper;
    @Autowired
    protected PropertyRepository propertyRepository;
    @Autowired
    protected PropertyDtoMapper propertyDtoMapper;
    @Autowired
    protected BookingService bookingService;
    @Autowired
    protected BookingMapper bookingMapper;
    @Autowired
    protected BookingRepository bookingRepository;
}
