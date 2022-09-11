package id.holigo.services.holigoaccountbalanceservice.web.mappers;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(AccountStatementMapperDecorator.class)
public interface AccountStatementMapper {
    @Mapping(target = "balance", source = "deposit")
    @Mapping(target = "debit", source = "debitAmount")
    @Mapping(target = "credit", source = "creditAmount")
    AccountStatement depositDtoToAccountStatement(DepositDto depositDto);

    @Mapping(target = "balance", source = "point")
    @Mapping(target = "debit", source = "debitAmount")
    @Mapping(target = "credit", source = "creditAmount")
    AccountStatement pointDtoToAccountStatement(PointDto pointDto);
}
