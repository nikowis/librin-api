package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
