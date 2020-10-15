package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.token.model.Token;
import pl.nikowis.librin.domain.token.model.TokenType;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByIdAndType(UUID id, TokenType type);
}
