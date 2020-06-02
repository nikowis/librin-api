package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Token;
import pl.nikowis.librin.model.TokenType;

import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    Token findByIdAndType(UUID id, TokenType type);
}
