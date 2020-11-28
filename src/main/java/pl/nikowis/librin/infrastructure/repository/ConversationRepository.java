package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.conversation.model.Conversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>, JpaSpecificationExecutor<Conversation> {

    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.offer o LEFT JOIN FETCH o.owner LEFT JOIN FETCH c.customer cust WHERE c.id = :convId AND (cust.id = :userId OR o.ownerId = :userId)")
    Optional<Conversation> findByIdAndCustomerIdOrOfferOwnerId(@Param("convId") Long convId, @Param("userId") Long userId);

    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.offer o LEFT JOIN FETCH o.owner LEFT JOIN FETCH o.buyer JOIN c.customer cust WHERE o.id = :offerId AND (cust.id = :userId OR o.ownerId = :userId)")
    Optional<Conversation> findByUserAndOfferId(@Param("offerId") Long offerId, @Param("userId") Long userId);

    List<Conversation> findAllByOfferId(@Param("offerId") Long offerId);

}
