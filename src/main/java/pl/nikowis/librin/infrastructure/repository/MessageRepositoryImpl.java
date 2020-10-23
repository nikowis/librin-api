package pl.nikowis.librin.infrastructure.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.message.model.Message;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void markMessagesAsRead(Long currentUserId, Long conversationId) {
        Query query = new Query(
                where("conversationId").is(conversationId)
                        .and("createdBy").ne(currentUserId)
                        .and("read").is(false)
        );

        mongoTemplate.updateMulti(query, new Update().set("read", true), Message.class);
    }
}
