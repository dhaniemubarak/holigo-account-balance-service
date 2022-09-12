package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.holigoaccountbalanceservice.services.AccountStatementService;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementPaginate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accountStatements")
public class AccountStatementController {

    private final AccountStatementService accountStatementService;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @GetMapping
    ResponseEntity<AccountStatementPaginate> getAccountStatement(@RequestHeader("user-id") Long userId,
                                                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                 @RequestParam(value = "type", required = false) AccountStatementType accountStatementType,
                                                                 @RequestParam(value = "startDate", required = false) Date startDate,
                                                                 @RequestParam(value = "endDate", required = false) Date endDate){

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return new ResponseEntity<>(accountStatementService.getAccountPaginate(userId, accountStatementType, startDate, endDate, PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);

    }

}
