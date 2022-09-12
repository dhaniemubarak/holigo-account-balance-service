package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.config.JmsConfig;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountBalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.jms.Message;
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
    public ResponseEntity<AccountBalanceDto> createAccountBalance(@RequestHeader("user-id") Long userId) {
        Optional<AccountBalance> fetchAccountBalance = accountBalanceRepository.findById(userId);
        return fetchAccountBalance.map(accountBalance -> new ResponseEntity<>(
                        accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalance), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
