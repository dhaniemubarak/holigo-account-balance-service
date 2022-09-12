package id.holigo.services.holigoaccountbalanceservice.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class AccountStatementPaginate extends PageImpl<AccountStatementDto> {

    @JsonCreator(mode = PROPERTIES)
    public AccountStatementPaginate(@JsonProperty("data") List<AccountStatementDto> content,
                               @JsonProperty("number") int number,
                               @JsonProperty("size") int size, @JsonProperty("totalElement") int totalElements,
                               @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                               @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                               @JsonProperty("first") boolean first, @JsonProperty("numberOfElement") int numberOfElement

    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public AccountStatementPaginate(List<AccountStatementDto> data, Pageable pageable, long total) {
        super(data, pageable, total);
    }

    public AccountStatementPaginate(List<AccountStatementDto> data) {
        super(data);
    }

}
