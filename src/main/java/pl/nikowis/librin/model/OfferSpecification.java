package pl.nikowis.librin.model;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import pl.nikowis.librin.dto.OfferFilterDTO;
import pl.nikowis.librin.model.Offer_;
import pl.nikowis.librin.model.User_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Data
public class OfferSpecification implements Specification<Offer> {

    private OfferFilterDTO filter;

    public OfferSpecification(OfferFilterDTO filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Offer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        /*
            Join fetch should be applied only for query to fetch the "data", not for "count" query to do pagination.
            Handled this by checking the criteriaQuery.getResultType(), if it's long that means query is
            for count so not appending join fetch else append it.
         */
        if (Long.class != query.getResultType()) {
            root.fetch(Offer_.owner, JoinType.LEFT);
            root.fetch(Offer_.buyer, JoinType.LEFT);
            root.fetch(Offer_.attachments, JoinType.LEFT);
        }

        if (filter.getOwner() != null) {
            predicates.add(builder.equal(root.get(Offer_.owner).get(User_.id), filter.getOwner()));
        }

        if (filter.getStatuses() != null && filter.getStatuses().size()>0) {
            predicates.add(root.get(Offer_.status).in(filter.getStatuses()));
        }

        if (filter.getAuthor() != null) {
            predicates.add(builder.like(builder.upper(root.get(Offer_.author)), "%" + filter.getAuthor().toUpperCase() + "%"));
        }

        if (filter.getTitle() != null) {
            predicates.add(builder.like(builder.upper(root.get(Offer_.title)), "%" + filter.getTitle().toUpperCase() + "%"));
        }

        if(filter.getCategories() != null) {
            predicates.add(root.get(Offer_.category).in(filter.getCategories()));
        }

        if(filter.getConditions() != null) {
            predicates.add(root.get(Offer_.condition).in(filter.getConditions()));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
