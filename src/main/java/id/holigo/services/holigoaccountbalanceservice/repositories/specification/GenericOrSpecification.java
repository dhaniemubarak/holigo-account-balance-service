package id.holigo.services.holigoaccountbalanceservice.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GenericOrSpecification<T> implements Specification<T> {

    private List<SearchCriteria> list;

    public GenericOrSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }
    
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for(SearchCriteria criteria :list){
            if(criteria.getOperation().equals(SearchOperation.EQUAL)){
                predicates.add(builder.equal(
                    root.get(criteria.getKey()),
                    criteria.getValue()));
            }
            else if(criteria.getOperation().equals(SearchOperation.MATCH)){
                predicates.add(builder.like(root.get(criteria.getKey()),
                     "%" + criteria.getValue().toString().toLowerCase() + "%"));
            }
            else if(criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().toString().toLowerCase() + "%"));
            }
        }

        if (list.isEmpty()){
            return builder.and();
        }
        return builder.or(predicates.toArray(new Predicate [0]));
    }
    
}
