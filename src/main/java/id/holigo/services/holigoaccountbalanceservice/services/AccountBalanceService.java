package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.AccountBalanceDto;

public interface AccountBalanceService {
    AccountBalanceDto createAccountBalance(AccountBalanceDto accountBalanceDto);

    AccountBalanceDto updateDeposit(AccountBalanceDto accountBalanceDto);

    AccountBalanceDto updatePoint(AccountBalanceDto accountBalanceDto);
}
