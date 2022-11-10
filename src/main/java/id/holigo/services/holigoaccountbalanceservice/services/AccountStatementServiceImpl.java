package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.*;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountStatementRepository;
import id.holigo.services.holigoaccountbalanceservice.repositories.specification.GenericAndSpecification;
import id.holigo.services.holigoaccountbalanceservice.repositories.specification.SearchCriteria;
import id.holigo.services.holigoaccountbalanceservice.repositories.specification.SearchOperation;
import id.holigo.services.holigoaccountbalanceservice.services.pushNotification.PushNotificationService;
import id.holigo.services.holigoaccountbalanceservice.web.mappers.AccountStatementMapper;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementPaginate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountStatementServiceImpl implements AccountStatementService {

    private AccountStatementMapper accountStatementMapper;

    private AccountStatementRepository accountStatementRepository;

    private AccountBalanceService accountBalanceService;

    private SearchCriteria.AccountStatementSpecification accountStatementSpecification;

    private PushNotificationService pushNotificationService;

    @Autowired
    public void setPushNotificationService(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @Autowired
    public void setAccountStatementSpecification(SearchCriteria.AccountStatementSpecification accountStatementSpecification) {
        this.accountStatementSpecification = accountStatementSpecification;
    }

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
        AccountStatement accountStatement;
        try {
            AccountBalanceDto accountBalanceDto = accountBalanceService.updateDeposit(
                    AccountBalanceDto.builder().userId(depositDto.getUserId())
                            .deposit(depositDto.getDeposit()
                                    .add(depositDto.getCreditAmount())
                                    .subtract(depositDto.getDebitAmount())).build()
            );
            if (accountBalanceDto == null) {
                throw new Exception();
            }

            accountStatement = accountStatementMapper.depositDtoToAccountStatement(depositDto);
            accountStatementRepository.save(accountStatement);
            if (accountStatement.getId() != null) {
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
        if (depositDto.getIsValid()) {
            sendNotification(accountStatement);
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

    @Override
    public AccountStatementPaginate getAccountPaginate(Long user, AccountStatementType accountStatementType, Date startDate, Date endDate, PageRequest pageRequest) {
        Page<AccountStatement> accountStatementPage;

        Specification<AccountStatement> getByType = accountStatementSpecification.getByType(accountStatementType);
        Specification<AccountStatement> getByDate = accountStatementSpecification.getDateBetween(startDate, endDate);

        GenericAndSpecification<AccountStatement> genericAndSpecification = new GenericAndSpecification<>();
        genericAndSpecification.add(new SearchCriteria("userId", user, SearchOperation.EQUAL));

        accountStatementPage = accountStatementRepository.findAll(Specification.where(getByDate).and(getByType).and(genericAndSpecification), pageRequest);


        return new AccountStatementPaginate(
                accountStatementPage
                        .getContent()
                        .stream()
                        .map(accountStatementMapper::accountStatementToAccountStatementDto)
                        .collect(Collectors.toList()),
                PageRequest.of(accountStatementPage.getPageable().getPageNumber(),
                        accountStatementPage.getPageable().getPageSize()),
                accountStatementPage.getTotalElements());
    }

    private void sendNotification(AccountStatement accountStatement) {
        try {
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            formatter.setDecimalFormatSymbols(symbols);
            String descriptionValue = accountStatement.getCredit().compareTo(BigDecimal.ZERO) > 0 ? formatter.format(accountStatement.getCredit()) : formatter.format(accountStatement.getDebit());
            PushNotificationDto pushNotificationDto = PushNotificationDto.builder()
                    .userId(accountStatement.getUserId())
                    .category(PushNotificationCategoryEnum.TRANSACTION)
                    .descriptionIndex(accountStatement.getInformationIndex() + "Description")
                    .descriptionValue(descriptionValue)
                    .titleIndex(accountStatement.getInformationIndex()).build();

            pushNotificationService.sendPushNotification(pushNotificationDto);
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
        }
    }
}
