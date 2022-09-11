package id.holigo.services.holigoaccountbalanceservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.AccountBalanceDto;
import id.holigo.services.holigoaccountbalanceservice.config.JmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;

@RestController
public class AccountBalanceController {

    private JmsTemplate jmsTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/api/v1/accountBalances")
    public ResponseEntity<AccountBalanceDto> createAccountBalance(@RequestBody AccountBalanceDto accountBalanceDto) throws JMSException, JsonProcessingException {
        Message received = jmsTemplate.sendAndReceive(JmsConfig.CREATE_ACCOUNT_BALANCE, session -> {
            Message message;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(accountBalanceDto));
                message.setStringProperty("_type", "id.holigo.services.common.model.AccountBalanceDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return message;

        });
        assert received != null;
        return new ResponseEntity<>(objectMapper.readValue(received.getBody(String.class),
                AccountBalanceDto.class), HttpStatus.CREATED);
    }
}
