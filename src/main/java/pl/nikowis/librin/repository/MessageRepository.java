package pl.nikowis.librin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
}
