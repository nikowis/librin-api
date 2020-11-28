package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.conversation.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long>, MessageRepositoryCustom {
    Page<Message> findByConversationIdOrderByCreatedAt(Long conversationId, Pageable pageable);
}
