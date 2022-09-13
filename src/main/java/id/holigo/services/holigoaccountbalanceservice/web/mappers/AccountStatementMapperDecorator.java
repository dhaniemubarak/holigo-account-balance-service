package id.holigo.services.holigoaccountbalanceservice.web.mappers;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class AccountStatementMapperDecorator implements AccountStatementMapper {

    private MessageSource messageSource;
    private AccountStatementMapper accountStatementMapper;

    @Autowired
    public void setAccountStatementMapper(AccountStatementMapper accountStatementMapper) {
        this.accountStatementMapper = accountStatementMapper;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public AccountStatement depositDtoToAccountStatement(DepositDto depositDto) {
        AccountStatement accountStatement = accountStatementMapper.depositDtoToAccountStatement(depositDto);
        accountStatement.setType(AccountStatementType.DEPOSIT);
        return accountStatement;
    }

    @Override
    public AccountStatement pointDtoToAccountStatement(PointDto pointDto) {
        AccountStatement accountStatement = accountStatementMapper.pointDtoToAccountStatement(pointDto);
        accountStatement.setType(AccountStatementType.POINT);
        return accountStatement;
    }

    @Override
    public AccountStatementDto accountStatementToAccountStatementDto(AccountStatement accountStatement) {
        AccountStatementDto accountStatementDto = accountStatementMapper.accountStatementToAccountStatementDto(accountStatement);
        accountStatementDto.setInformation(messageSource.getMessage(accountStatement.getInformationIndex(), null, LocaleContextHolder.getLocale()));
        return accountStatementDto;
    }
}
