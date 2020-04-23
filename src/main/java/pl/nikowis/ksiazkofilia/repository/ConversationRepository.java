package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
