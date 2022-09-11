package id.holigo.services.holigoaccountbalanceservice.services;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;

public interface AccountStatementService {
    DepositDto createStatement(DepositDto depositDto);

    PointDto createStatement(PointDto pointDto);
}
