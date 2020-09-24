package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.message.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
}
