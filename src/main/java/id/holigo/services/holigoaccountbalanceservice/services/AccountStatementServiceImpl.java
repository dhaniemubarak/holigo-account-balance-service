package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountStatementRepository;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountStatementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AccountStatementServiceImpl implements AccountStatementService {

    private AccountStatementMapper accountStatementMapper;

    private AccountStatementRepository accountStatementRepository;

    private AccountBalanceService accountBalanceService;

    @Autowired
    public void setAccountBalanceService(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @Autowired
    public void setAccountStatementRepository(AccountStatementRepository accountStatementRepository) {
        this.accountStatementRepository = accountStatementRepository;
    }

    @Autowired
    public void setAccountStatementMapper(AccountStatementMapper accountStatementMapper) {
        this.accountStatementMapper = accountStatementMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DepositDto createStatement(DepositDto depositDto) {
        try {
            AccountBalanceDto accountBalanceDto = accountBalanceService.updateDeposit(
                    AccountBalanceDto.builder().userId(depositDto.getUserId())
                            .deposit(depositDto.getDeposit()
                                    .add(depositDto.getCreditAmount())
                                    .subtract(depositDto.getDebitAmount())).build()
            );
            if (accountBalanceDto == null) {
                log.info("accountBalanceDto is not null");
                throw new Exception();
            }

            AccountStatement accountStatement = accountStatementMapper.depositDtoToAccountStatement(depositDto);
            accountStatementRepository.save(accountStatement);
            if (accountStatement.getId() != null) {
                log.info("accountStatement is not null");

                log.info("accountBalanceDto deposit -> {}", accountBalanceDto.getDeposit());
                if (accountBalanceDto.getDeposit().equals(depositDto.getDeposit()
                        .add(depositDto.getCreditAmount())
                        .subtract(depositDto.getDebitAmount()))) {
                    depositDto.setIsValid(true);
                    depositDto.setDeposit(accountBalanceDto.getDeposit());
                }
            }

        } catch (Exception e) {
            log.info("Catch e -> {}", e.getMessage());
            return depositDto;
        }
        return depositDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PointDto createStatement(PointDto pointDto) {
        try {
            AccountBalanceDto accountBalanceDto = accountBalanceService.updatePoint(
                    AccountBalanceDto.builder().userId(pointDto.getUserId())
                            .point(pointDto.getPoint()
                                    + pointDto.getCreditAmount()
                                    - pointDto.getDebitAmount()).build()
            );
            if (accountBalanceDto == null) {
                log.info("accountBalanceDto is not null");
                throw new Exception();
            }

            AccountStatement accountStatement = accountStatementMapper.pointDtoToAccountStatement(pointDto);
            accountStatementRepository.save(accountStatement);
            if (accountStatement.getId() != null) {
                log.info("accountStatement is not null");
                log.info("accountBalanceDto point -> {}", accountBalanceDto.getPoint());
                if (accountBalanceDto.getPoint().equals(pointDto.getPoint()
                        + pointDto.getCreditAmount()
                        - pointDto.getDebitAmount())) {
                    pointDto.setIsValid(true);
                    pointDto.setPoint(accountBalanceDto.getPoint());
                }
            }

        } catch (Exception e) {
            log.info("Catch e -> {}", e.getMessage());
            return pointDto;
        }
        return pointDto;
    }
}
