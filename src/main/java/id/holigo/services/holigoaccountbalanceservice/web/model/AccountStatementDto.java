package id.holigo.services.holigoaccountbalanceservice.web.model;

import id.holigo.services.common.model.AccountStatementType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class AccountStatementDto implements Serializable {

    private UUID id;

    private Long userId;

    private AccountStatementType type;

    private BigDecimal balance;

    private BigDecimal debit;

    private BigDecimal credit;

    private String informationIndex;

    private String informationValue;

    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID transactionId;

    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID paymentId;

    private String transactionType;

    private String invoiceNumber;

    /**
     * PAYMENT
     * REFUND
     * TOP_UP
     * WITHDRAWAL
     */
    private String category;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
