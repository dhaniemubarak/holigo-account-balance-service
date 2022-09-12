package id.holigo.services.holigoaccountbalanceservice.repositories;

import id.holigo.services.holigoaccountbalanceservice.domain.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AccountStatementRepository extends JpaRepository<AccountStatement, UUID> , JpaSpecificationExecutor<AccountStatement> {
}
