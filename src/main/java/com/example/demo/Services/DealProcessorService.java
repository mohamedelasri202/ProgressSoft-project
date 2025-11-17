//package com.example.demo.Services;
//
//import com.example.demo.DTO.DealDto;
//
//import com.example.demo.Models.Deal;
//import com.example.demo.Repositories.DealRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.ZoneOffset;
//
//@Slf4j
//@Service
//public class DealProcessorService {
//    private final DealRepository dealRepository;
//
//    public DealProcessorService(DealRepository dealRepository) {
//        this.dealRepository = dealRepository;
//    }
//
//    /**
//     * Saves a single validated DealDTO to the database within an isolated transaction.
//     * * The @Transactional annotation ensures that:
//     * 1. The deal is committed to the DB if successful.
//     * 2. If any error occurs (e.g., unexpected DB issue), only this single save operation
//     * rolls back, allowing the outer batch loop to continue.
//     * * Propagation.REQUIRES_NEW explicitly forces a new, independent transaction to start.
//     *
//     * @param dealDto The validated deal object to be persisted.
//     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void saveDeal(DealDto dealDto) {
//        log.debug("Attempting to save single deal: {}", dealDto.getDealUniqueId());
//
//
//        Deal deal = mapToEntity(dealDto);
//
//        // 2. Persist the Entity
//        dealRepository.save(deal);
//
//        log.info("Successfully committed deal: {}", deal.getDealUniqueId());
//    }
//
//    /**
//     * Simple mapping logic from the input DTO to the database Entity.
//     * @param dto The DealDTO
//     * @return The Deal JPA Entity
//     */
//    private Deal mapToEntity(DealDto dto) {
//        // Use ZoneOffset.UTC for consistent handling of Instant
//        return new Deal(
//                dto.getDealUniqueId(),
//                dto.getFromCurrencyIsoCode(),
//                dto.getToCurrencyIsoCode(),
//                dto.getDealTimestamp().atZone(ZoneOffset.UTC).toInstant(),
//                dto.getDealAmount()
//        );
//    }
//}
