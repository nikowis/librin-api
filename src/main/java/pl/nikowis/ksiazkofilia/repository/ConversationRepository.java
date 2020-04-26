package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.offer o JOIN c.customer cust WHERE cust.id = :userId OR o.ownerId = :userId")
    Page<Conversation> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
