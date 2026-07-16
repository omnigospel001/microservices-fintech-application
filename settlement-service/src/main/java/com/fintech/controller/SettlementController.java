package com.fintech.controller;

import com.fintech.enums.SettlementStatus;
import com.fintech.request.SettlementRequest;
import com.fintech.response.SettlementResponse;
import com.fintech.service.SettlementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<SettlementResponse> create(@RequestBody @Valid SettlementRequest request) {
        var settlement = settlementService.createSettlement(
                request.getTransactionType(),
                request.getReferenceId(),
                request.getUserId(),
                null,
                request.getAmount(),
                request.getStatus()
        );
        return ResponseEntity.ok(settlementService.findById(settlement.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettlementResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(settlementService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SettlementResponse>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(settlementService.findByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SettlementResponse>> findByStatus(@PathVariable SettlementStatus status) {
        return ResponseEntity.ok(settlementService.findByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<SettlementResponse>> findAll() {
        return ResponseEntity.ok(settlementService.findAll());
    }

    @PostMapping("/process/{id}")
    public ResponseEntity<SettlementResponse> process(@PathVariable Integer id) {
        return ResponseEntity.ok(settlementService.processSettlement(id));
    }

    @PostMapping("/batch")
    public ResponseEntity<Integer> batchProcess() {
        return ResponseEntity.ok(settlementService.batchProcessPendingSettlements());
    }

}
