package id.holigo.services.holigoaccountbalanceservice.repositories;

import id.holigo.services.holigoaccountbalanceservice.domain.QueueCreateAccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueCreateAccountBalanceRepository extends JpaRepository<QueueCreateAccountBalance, Long> {

    List<QueueCreateAccountBalance> findAllByIsCreated(Boolean isCreated);
}
