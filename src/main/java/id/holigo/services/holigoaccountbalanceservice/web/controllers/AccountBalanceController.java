package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import id.holigo.services.holigoaccountbalanceservice.domain.QueueCreateAccountBalance;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.repositories.QueueCreateAccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountBalanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@Slf4j
public class AccountBalanceController {
    private AccountBalanceRepository accountBalanceRepository;
    private AccountBalanceMapper accountBalanceMapper;

    private QueueCreateAccountBalanceRepository queueCreateAccountBalanceRepository;

    @Autowired
    public void setQueueCreateAccountBalanceRepository(QueueCreateAccountBalanceRepository queueCreateAccountBalanceRepository) {
        this.queueCreateAccountBalanceRepository = queueCreateAccountBalanceRepository;
    }

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
        return fetchAccountBalance.map(accountBalance -> new ResponseEntity<>(accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalance), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/api/v1/depositAccountBalance")
    public ResponseEntity<HttpStatus> initDeposit(@RequestHeader("user-id") Long userId) {
        Optional<AccountBalance> fetchAccountBalance = accountBalanceRepository.findById(userId);
        if (fetchAccountBalance.isEmpty()) {
            try {
                AccountBalance accountBalance = new AccountBalance();
                accountBalance.setUserId(userId);
                accountBalance.setDeposit(BigDecimal.valueOf(0.00));
                accountBalanceRepository.save(accountBalance);
            } catch (Exception e) {
                log.error(e.getMessage());
                QueueCreateAccountBalance queueCreateAccountBalance = QueueCreateAccountBalance.builder()
                        .userId(userId)
                        .isCreated(false)
                        .deposit(BigDecimal.valueOf(0.00)).build();
                queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
            }
        } else {
            AccountBalance accountBalance = fetchAccountBalance.get();
            if (accountBalance.getDeposit() == null) {
                try {
                    accountBalance.setDeposit(BigDecimal.valueOf(0.00));
                    accountBalanceRepository.save(accountBalance);
                } catch (Exception e) {
                    QueueCreateAccountBalance queueCreateAccountBalance = QueueCreateAccountBalance.builder()
                            .userId(userId)
                            .isCreated(false)
                            .deposit(BigDecimal.valueOf(0.00)).build();
                    queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/pointAccountBalance")
    public ResponseEntity<HttpStatus> initPoint(@RequestHeader("user-id") Long userId) {
        Optional<AccountBalance> fetchAccountBalance = accountBalanceRepository.findById(userId);
        if (fetchAccountBalance.isEmpty()) {
            try {
                AccountBalance accountBalance = new AccountBalance();
                accountBalance.setUserId(userId);
                accountBalance.setPoint(0);
                accountBalanceRepository.save(accountBalance);
            } catch (Exception e) {
                QueueCreateAccountBalance queueCreateAccountBalance = QueueCreateAccountBalance.builder()
                        .userId(userId)
                        .isCreated(false)
                        .point(0).build();
                queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
            }
        } else {
            AccountBalance accountBalance = fetchAccountBalance.get();
            if (accountBalance.getPoint() == null) {
                try {
                    accountBalance.setPoint(0);
                    accountBalanceRepository.save(accountBalance);
                } catch (Exception e) {
                    QueueCreateAccountBalance queueCreateAccountBalance = QueueCreateAccountBalance.builder()
                            .userId(userId)
                            .isCreated(false)
                            .point(0).build();
                    queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
