package id.holigo.services.holigoaccountbalanceservice.repositories;

import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    @Modifying(flushAutomatically = true)
    @Query("update AccountBalance a set a.deposit=:newDeposit where a.userId=:userId and  a.deposit=:deposit")
    int updateDeposit(@Param("userId") Long userId,
                      @Param("deposit") BigDecimal deposit,
                      @Param("newDeposit") BigDecimal newDeposit);

    @Modifying(flushAutomatically = true)
    @Query("update AccountBalance a set a.point=:newPoint where a.userId=:userId and a.point=:point")
    int updatePoint(@Param("userId") Long userId,
                    @Param("point") Integer point,
                    @Param("newPoint") Integer newPoint);
}
