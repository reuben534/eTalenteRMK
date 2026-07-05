package com.enviro.assessment.junior.reuben.controller;

import com.enviro.assessment.junior.reuben.domain.WithdrawalNotice;
import com.enviro.assessment.junior.reuben.dto.CreateWithdrawalRequest;
import com.enviro.assessment.junior.reuben.dto.WithdrawalNoticeDto;
import com.enviro.assessment.junior.reuben.service.CsvStatementService;
import com.enviro.assessment.junior.reuben.service.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/withdrawals")
@Tag(name = "Withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final CsvStatementService csvStatementService;

    public WithdrawalController(WithdrawalService withdrawalService, CsvStatementService csvStatementService) {
        this.withdrawalService = withdrawalService;
        this.csvStatementService = csvStatementService;
    }

    @PostMapping
    @Operation(summary = "Create a withdrawal notice")
    public ResponseEntity<WithdrawalNoticeDto> create(@Valid @RequestBody CreateWithdrawalRequest request) {
        return ResponseEntity.status(201).body(withdrawalService.create(request));
    }

    @GetMapping
    @Operation(summary = "List withdrawal notices for an investor")
    public List<WithdrawalNoticeDto> history(@RequestParam Long investorId) {
        return withdrawalService.history(investorId);
    }

    @GetMapping(value = "/statement.csv", produces = "text/csv")
    @Operation(summary = "Export filtered CSV withdrawal statement")
    public ResponseEntity<String> statement(
            @RequestParam Long investorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<WithdrawalNotice> rows = withdrawalService.statementRows(investorId, from, to);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=withdrawal-statement.csv")
                .body(csvStatementService.build(rows));
    }
}
