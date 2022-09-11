package id.holigo.services.holigoaccountbalanceservice.web.mappers;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AccountStatementMapperDecorator implements AccountStatementMapper {
    private AccountStatementMapper accountStatementMapper;

    @Autowired
    public void setAccountStatementMapper(AccountStatementMapper accountStatementMapper) {
        this.accountStatementMapper = accountStatementMapper;
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
}
