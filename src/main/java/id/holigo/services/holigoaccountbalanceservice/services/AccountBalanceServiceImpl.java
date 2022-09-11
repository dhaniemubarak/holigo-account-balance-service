package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountBalanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private AccountBalanceMapper accountBalanceMapper;

    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    public void setAccountBalanceRepository(AccountBalanceRepository accountBalanceRepository) {
        this.accountBalanceRepository = accountBalanceRepository;
    }

    @Autowired
    public void setAccountBalanceMapper(AccountBalanceMapper accountBalanceMapper) {
        this.accountBalanceMapper = accountBalanceMapper;
    }

    @Override
    public AccountBalanceDto createAccountBalance(AccountBalanceDto accountBalanceDto) {
        if (!accountBalanceRepository.existsById(accountBalanceDto.getUserId())) {
            AccountBalance accountBalance = accountBalanceMapper.accountBalanceDtoToAccountBalance(accountBalanceDto);
            return accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalanceRepository.save(accountBalance));
        } else {
            AccountBalance accountBalance = accountBalanceRepository.getById(accountBalanceDto.getUserId());
            if (accountBalance.getPoint() == null) {
                accountBalance.setPoint(accountBalanceDto.getPoint());
                accountBalanceRepository.save(accountBalance);
            }
            if (accountBalance.getDeposit() == null) {
                accountBalance.setDeposit(accountBalanceDto.getDeposit());
                accountBalanceRepository.save(accountBalance);
            }
        }
        return accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalanceRepository.getById(accountBalanceDto.getUserId()));
    }

    @Override
    public AccountBalanceDto updateDeposit(AccountBalanceDto accountBalanceDto) {
        log.info("updateDeposit is running...");
        boolean isExists = accountBalanceRepository.existsById(accountBalanceDto.getUserId());
        log.info("exists value is {}", isExists);
        if (!isExists) {
            createAccountBalance(AccountBalanceDto.builder()
                    .userId(accountBalanceDto.getUserId())
                    .deposit(BigDecimal.ZERO).build());
        }
        AccountBalance accountBalance = accountBalanceRepository.getById(accountBalanceDto.getUserId());
        if (accountBalance.getDeposit() == null) {
            log.info("point is null...");
            AccountBalanceDto createDeposit = createAccountBalance(AccountBalanceDto.builder().userId(accountBalanceDto.getUserId()).deposit(BigDecimal.ZERO).build());
            accountBalance.setDeposit(createDeposit.getDeposit());
        }
        int updateDeposit = accountBalanceRepository.updateDeposit(accountBalance.getUserId(),
                accountBalance.getDeposit(), accountBalanceDto.getDeposit());
        if (updateDeposit == 0) {
            return null;
        }
        accountBalance.setDeposit(accountBalanceDto.getDeposit());
        log.info("newAccountBalance -> {}", accountBalance.getDeposit());
        return accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalance);
    }

    @Override
    public AccountBalanceDto updatePoint(AccountBalanceDto accountBalanceDto) {
        log.info("updatePoint is running...");
        boolean isExists = accountBalanceRepository.existsById(accountBalanceDto.getUserId());
        if (!isExists) {
            log.info("isExists is running...");
            createAccountBalance(AccountBalanceDto.builder().userId(accountBalanceDto.getUserId()).point(0).build());
        }
        AccountBalance accountBalance = accountBalanceRepository.getById(accountBalanceDto.getUserId());
        if (accountBalance.getPoint() == null) {
            log.info("point is null...");
            AccountBalanceDto createPoint = createAccountBalance(AccountBalanceDto.builder().userId(accountBalanceDto.getUserId()).point(0).build());
            accountBalance.setPoint(createPoint.getPoint());
        }
        int updatePoint = accountBalanceRepository.updatePoint(accountBalance.getUserId(),
                accountBalance.getPoint(), accountBalanceDto.getPoint());
        if (updatePoint == 0) {
            log.info("update point == 0");
            return null;
        }
        accountBalance.setPoint(accountBalanceDto.getPoint());
        log.info("newAccountBalance point  -> {}", accountBalance.getPoint());
        return accountBalanceMapper.accountBalanceToAccountBalanceDto(accountBalance);
    }
}
