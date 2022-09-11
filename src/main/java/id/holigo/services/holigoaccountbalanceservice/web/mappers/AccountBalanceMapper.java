package id.holigo.services.holigoaccountbalanceservice.web.mappers;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import org.mapstruct.Mapper;

@Mapper
public interface AccountBalanceMapper {

    AccountBalance accountBalanceDtoToAccountBalance(AccountBalanceDto accountBalanceDto);

    AccountBalanceDto accountBalanceToAccountBalanceDto(AccountBalance accountBalance);
}
