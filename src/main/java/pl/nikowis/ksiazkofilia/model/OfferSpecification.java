package pl.nikowis.ksiazkofilia.model;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import pl.nikowis.ksiazkofilia.dto.OfferFilterDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

        if (filter.getOwner() != null) {
            predicates.add(builder.equal(root.get(Offer_.owner).get(User_.id), filter.getOwner()));
        }

        if (filter.getStatus() != null) {
            predicates.add(builder.equal(root.get(Offer_.status), filter.getStatus()));
        }

        if (filter.getAuthor() != null) {
            predicates.add(builder.like(builder.upper(root.get(Offer_.author)), "%" + filter.getAuthor().toUpperCase() + "%"));
        }

        if (filter.getTitle() != null) {
            predicates.add(builder.like(builder.upper(root.get(Offer_.title)), "%" + filter.getTitle().toUpperCase() + "%"));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
