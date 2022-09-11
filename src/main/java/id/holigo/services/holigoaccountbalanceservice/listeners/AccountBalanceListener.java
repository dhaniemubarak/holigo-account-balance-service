package id.holigo.services.holigoaccountbalanceservice.listeners;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.config.JmsConfig;
import id.holigo.services.holigoaccountbalanceservice.services.AccountBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Component
public class AccountBalanceListener {

    private AccountBalanceService accountBalanceService;

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Autowired
    public void setAccountBalanceService(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @Transactional
    @JmsListener(destination = JmsConfig.CREATE_ACCOUNT_BALANCE)
    public void listenForCreateAccountBalance(@Payload AccountBalanceDto accountBalanceDto, @Headers MessageHeaders headers, Message message) throws JMSException {
        log.info("listenForCreateAccountBalance is running...");
        log.info("AccountBalanceDto -> {}", accountBalanceDto);
        AccountBalanceDto newAccountBalance = accountBalanceService.createAccountBalance(accountBalanceDto);
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), newAccountBalance);

    }
}
