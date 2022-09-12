package id.holigo.services.holigoaccountbalanceservice.domain;

import id.holigo.services.common.model.AccountStatementType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;

@Component
public class AccountStatementSpecification {

    public Specification<AccountStatement> getDateBetween(Date startDate, Date endDate){

        return (root, query, criteriaBuilder) -> {

            if (endDate == null || startDate == null) {
                return criteriaBuilder.and();
            } else {

                return criteriaBuilder.between(root.get("createdAt"), new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
            }

        };
    }

    public Specification<AccountStatement> getByType(AccountStatementType accountStatementType){

        return ((root, query, criteriaBuilder) -> {

            if (accountStatementType == null){
                return criteriaBuilder.and();
            }else{
                return criteriaBuilder.equal(root.get("type"), accountStatementType);
            }

        });

    }

}
