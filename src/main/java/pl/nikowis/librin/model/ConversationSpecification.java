package pl.nikowis.librin.model;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Data
public class ConversationSpecification implements Specification<Conversation> {

    private Long userId;

    public ConversationSpecification(Long userId) {
        this.userId = userId;
    }

    @Override
    public Predicate toPredicate(Root<Conversation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new LinkedList<>();

        /*
            Join fetch should be applied only for query to fetch the "data", not for "count" query to do pagination.
            Handled this by checking the criteriaQuery.getResultType(), if it's long that means query is
            for count so not appending join fetch else append it.
         */
        if (Long.class != query.getResultType()) {
            Fetch<Conversation, Offer> fetchedOffer = root.fetch(Conversation_.offer, JoinType.LEFT);
            fetchedOffer.fetch(Offer_.owner, JoinType.LEFT);
            fetchedOffer.fetch(Offer_.buyer, JoinType.LEFT);
            fetchedOffer.fetch(Offer_.attachments, JoinType.LEFT);
            root.fetch(Conversation_.customer, JoinType.LEFT);
        }

        Predicate isCustomer = builder.equal(root.get(Conversation_.customer).get(User_.id), userId);
        Predicate isOfferOwner = builder.equal(root.get(Conversation_.offer).get(Offer_.owner).get(User_.id), userId);
        predicates.add(builder.or(isCustomer, isOfferOwner));
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
