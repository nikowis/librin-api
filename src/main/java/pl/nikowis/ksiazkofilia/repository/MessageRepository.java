package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
