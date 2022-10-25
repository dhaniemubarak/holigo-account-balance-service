package id.holigo.services.holigoaccountbalanceservice.repositories;

import id.holigo.services.common.model.AccountStatementType;
import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface AccountStatementRepository extends JpaRepository<AccountStatement, UUID>, JpaSpecificationExecutor<AccountStatement> {

    Optional<AccountStatement> findFirstByUserIdAndInvoiceNumberAndTypeAndCreditGreaterThan(Long userId, String invoiceNumber, AccountStatementType accountStatementType, BigDecimal pointAmount);
}
