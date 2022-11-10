package id.holigo.services.holigoaccountbalanceservice.schedulers;

import id.holigo.services.holigoaccountbalanceservice.domain.AccountBalance;
import id.holigo.services.holigoaccountbalanceservice.domain.QueueCreateAccountBalance;
import id.holigo.services.holigoaccountbalanceservice.repositories.AccountBalanceRepository;
import id.holigo.services.holigoaccountbalanceservice.repositories.QueueCreateAccountBalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class QueueAccountBalance {

    private QueueCreateAccountBalanceRepository queueCreateAccountBalanceRepository;

    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    public void setAccountBalanceRepository(AccountBalanceRepository accountBalanceRepository) {
        this.accountBalanceRepository = accountBalanceRepository;
    }

    @Autowired
    public void setQueueCreateAccountBalanceRepository(QueueCreateAccountBalanceRepository queueCreateAccountBalanceRepository) {
        this.queueCreateAccountBalanceRepository = queueCreateAccountBalanceRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    void checkQueue() {
        List<QueueCreateAccountBalance> queueCreateAccountBalanceList = queueCreateAccountBalanceRepository.findAllByIsCreated(false);
        queueCreateAccountBalanceList.forEach(queueCreateAccountBalance -> {
            Optional<AccountBalance> fetchAccountBalance = accountBalanceRepository.findById(queueCreateAccountBalance.getUserId());
            if (fetchAccountBalance.isPresent()) {
                AccountBalance accountBalance = fetchAccountBalance.get();
                if (queueCreateAccountBalance.getDeposit() != null && accountBalance.getDeposit() == null) {
                    try {
                        accountBalance.setDeposit(queueCreateAccountBalance.getDeposit());
                        accountBalanceRepository.save(accountBalance);
                        queueCreateAccountBalance.setIsCreated(true);
                    } catch (Exception e) {
                        log.error("Error : {}", e.getMessage());
                    }
                } else {
                    queueCreateAccountBalance.setIsCreated(true);
                }
                if (queueCreateAccountBalance.getPoint() != null && accountBalance.getPoint() == null) {
                    try {
                        accountBalance.setPoint(queueCreateAccountBalance.getPoint());
                        accountBalanceRepository.save(accountBalance);
                        queueCreateAccountBalance.setIsCreated(true);
                    } catch (Exception e) {
                        log.error("Error : {}", e.getMessage());
                    }
                } else {
                    queueCreateAccountBalance.setIsCreated(true);
                }
                queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
            } else {
                try {
                    AccountBalance accountBalance = new AccountBalance();
                    accountBalance.setUserId(queueCreateAccountBalance.getUserId());
                    accountBalance.setPoint(queueCreateAccountBalance.getPoint());
                    accountBalance.setDeposit(queueCreateAccountBalance.getDeposit());
                    accountBalanceRepository.save(accountBalance);
                    queueCreateAccountBalance.setIsCreated(true);
                    queueCreateAccountBalanceRepository.save(queueCreateAccountBalance);
                } catch (Exception e) {
                    log.error("Error : {}", e.getMessage());
                }
            }
        });
    }
}
