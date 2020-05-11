package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Consent;
import pl.nikowis.ksiazkofilia.model.Token;
import pl.nikowis.ksiazkofilia.model.TokenType;

import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    Token findByIdAndType(UUID id, TokenType type);
}
