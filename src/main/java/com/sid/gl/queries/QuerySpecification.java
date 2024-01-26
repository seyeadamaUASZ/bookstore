package com.sid.gl.queries;

import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuerySpecification<T> implements Specification<T> {
    private final List<SearchCriteria> list = new ArrayList<>();

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @SneakyThrows
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates =
                new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (SearchCriteria criteria : list) {
            switch (criteria.getSearchOperation()) {
                case EQUAL:
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                    break;
                case LIKE:
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case GREATER_THAN_EQUAL:
                    Date startDate = formatter.parse(criteria.getValue().toString());
                    predicates.add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), startDate));
                    break;
                case LESS_THAN_EQUAL:
                    Date endDate = formatter.parse(criteria.getValue().toString());
                    predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), endDate));
                    break;
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
