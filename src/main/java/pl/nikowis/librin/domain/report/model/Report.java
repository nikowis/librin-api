package pl.nikowis.librin.domain.report.model;

import lombok.Data;
import pl.nikowis.librin.domain.conversation.model.Conversation;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @Size(max = 512)
    private String description;

    @ManyToOne
    @JoinColumn(name = "reporterId")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private boolean examined;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

}
