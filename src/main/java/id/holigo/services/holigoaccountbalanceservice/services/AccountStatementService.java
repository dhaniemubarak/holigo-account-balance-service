package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.web.model.AccountStatementPaginate;
import org.springframework.data.domain.PageRequest;

import java.sql.Date;

public interface AccountStatementService {
    DepositDto createStatement(DepositDto depositDto);

    PointDto createStatement(PointDto pointDto);

    AccountStatementPaginate getAccountPaginate(Long user, AccountStatementType accountStatementType, Date startDate, Date endDate, PageRequest pageRequest);

}
