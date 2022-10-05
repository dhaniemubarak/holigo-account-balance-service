package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountBalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountBalanceController {
    private AccountBalanceRepository accountBalanceRepository;
    private AccountBalanceMapper accountBalanceMapper;

    @Autowired
    public void setAccountBalanceRepository(AccountBalanceRepository accountBalanceRepository) {
        this.accountBalanceRepository = accountBalanceRepository;
    }

    @Autowired
    public void setAccountBalanceMapper(AccountBalanceMapper accountBalanceMapper) {
        this.accountBalanceMapper = accountBalanceMapper;
    }

    @GetMapping("/api/v1/accountBalance")
    public ResponseEntity<AccountBalanceDto> getAccountBalance(@RequestHeader("user-id") Long userId) {
        Optional<AccountBalance> fetchAccountBalance = accountBalanceRepository.findById(userId);
        if (fetchAccountBalance.isPresent()) {
            return new ResponseEntity<>(accountBalanceMapper.accountBalanceToAccountBalanceDto(fetchAccountBalance.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
