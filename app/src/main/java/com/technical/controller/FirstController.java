package com.technical.controller;

import com.technical.FirstService;
import com.technical.dto.FirstDto;

import com.technical.dto.FirstDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/first")
@Slf4j
public class FirstController {

    FirstService firstService;
    FirstDtoMapper mapper;

    @Autowired
    public FirstController(final FirstService firstService, final FirstDtoMapper mapper) {
        this.firstService = firstService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/")
    public ResponseEntity<FirstDto> saveFirst(@Valid @RequestBody final FirstDto firstDto) {
        addLoggingContext(firstDto);
        log.info("Saving first: {}", firstDto);
        final var savedFirst = firstService.create(mapper.toBusiness(firstDto));
        final var savedFirstDto = mapper.toDto(savedFirst);
        return ResponseEntity.ok(savedFirstDto);

    }

    private static void addLoggingContext(final FirstDto firstDto) {
        MDC.put("FirstName", firstDto.getName());
    }
}
