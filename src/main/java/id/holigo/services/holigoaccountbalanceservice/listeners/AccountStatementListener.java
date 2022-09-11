package id.holigo.services.holigoaccountbalanceservice.listeners;

import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.PointDto;
import id.holigo.services.holigoaccountbalanceservice.config.JmsConfig;
import id.holigo.services.holigoaccountbalanceservice.services.AccountStatementService;
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
public class AccountStatementListener {

    private JmsTemplate jmsTemplate;

    private AccountStatementService accountStatementService;

    @Autowired
    public void setAccountStatementService(AccountStatementService accountStatementService) {
        this.accountStatementService = accountStatementService;
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.CREATE_DEPOSIT_ACCOUNT_STATEMENT)
    public void listenForCreateDepositAccountStatement(@Payload DepositDto depositDto, @Headers MessageHeaders headers, Message message) throws JMSException {
        log.info("listenForCreateAccountStatement is running...");
        try {
            accountStatementService.createStatement(depositDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), depositDto);
    }

    @JmsListener(destination = JmsConfig.CREATE_POINT_ACCOUNT_STATEMENT)
    public void listenForCreatePointAccountStatement(@Payload PointDto pointDto, @Headers MessageHeaders headers, Message message) throws JMSException {
        log.info("listenForCreateAccountStatement is running...");
        try {
            accountStatementService.createStatement(pointDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), pointDto);
    }
}
