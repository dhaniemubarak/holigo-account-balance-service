package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountStatementRepository;
import id.holigo.services.holigoaccountbalanceservice.services.AccountStatementService;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountStatementMapper;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementDto;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementPaginate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccountStatementController {

    private AccountStatementService accountStatementService;

    private AccountStatementRepository accountStatementRepository;

    private AccountStatementMapper accountStatementMapper;


    @Autowired
    public void setAccountStatementMapper(AccountStatementMapper accountStatementMapper) {
        this.accountStatementMapper = accountStatementMapper;
    }

    @Autowired
    public void setAccountStatementRepository(AccountStatementRepository accountStatementRepository) {
        this.accountStatementRepository = accountStatementRepository;
    }

    @Autowired
    public void setAccountStatementService(AccountStatementService accountStatementService) {
        this.accountStatementService = accountStatementService;
    }

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @GetMapping("/accountStatements")
    ResponseEntity<AccountStatementPaginate> getAccountStatement(@RequestHeader("user-id") Long userId,
                                                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                 @RequestParam(value = "type", required = false) AccountStatementType accountStatementType,
                                                                 @RequestParam(value = "startDate", required = false) Date startDate,
                                                                 @RequestParam(value = "endDate", required = false) Date endDate) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return new ResponseEntity<>(accountStatementService.getAccountPaginate(userId, accountStatementType, startDate, endDate, PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending())), HttpStatus.OK);

    }

    @GetMapping("/creditAccountStatement")
    ResponseEntity<AccountStatementDto> getSingleAccountStatement(@RequestHeader("user-id") Long userId,
                                                                  @RequestParam(value = "invoiceNumber") String invoiceNumber,
                                                                  @RequestParam(value = "type") AccountStatementType type) {
        Optional<AccountStatement> fetchAccountStatement = accountStatementRepository
                .findFirstByUserIdAndInvoiceNumberAndTypeAndCreditGreaterThan(userId, invoiceNumber, type, BigDecimal.ZERO);
        return fetchAccountStatement.map(accountStatement -> new ResponseEntity<>(accountStatementMapper.accountStatementToAccountStatementDto(accountStatement), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
