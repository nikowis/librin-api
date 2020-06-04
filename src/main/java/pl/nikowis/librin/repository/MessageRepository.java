package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("UPDATE Message m SET m.read=true WHERE m.id IN " +
            "(" +
            " SELECT m1.id FROM Message m1 " +
            " JOIN m1.conversation conv" +
            " WHERE conv.id =:convId AND m1.createdBy <> :readerId" +
            ")")
    void markMessagesAsRead(@Param("convId") Long convId, @Param("readerId") Long readerId);
}
