package pl.nikowis.librin.infrastructure.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepositoryCustom {

    void markMessagesAsRead(Long currentUserId, Long conversationId);
}
