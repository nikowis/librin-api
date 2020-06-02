package pl.nikowis.librin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Conversation;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.offer o JOIN c.customer cust WHERE cust.id = :userId OR o.ownerId = :userId")
    Page<Conversation> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Conversation c JOIN c.offer o JOIN c.customer cust WHERE c.id = :convId AND (cust.id = :userId OR o.ownerId = :userId)")
    Optional<Conversation> findByIdAndCustomerIdOrOfferOwnerId(@Param("convId") Long convId, @Param("userId") Long userId);

    @Query("SELECT c FROM Conversation c JOIN c.offer o JOIN c.customer cust WHERE o.id = :offerId AND (cust.id = :userId OR o.ownerId = :userId)")
    Optional<Conversation> findByUserAndOfferId(@Param("offerId") Long offerId, @Param("userId") Long userId);
}
