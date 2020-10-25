package pl.nikowis.librin.domain.token.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.nikowis.librin.domain.user.exception.TokenNotFoundException;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    protected LocalDateTime createdAt;

    protected LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private boolean executed;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void validateAndExecute() {
        if (expiresAt.isBefore(LocalDateTime.now()) || executed) {
            throw new TokenNotFoundException();
        }
        executed = true;
    }
}