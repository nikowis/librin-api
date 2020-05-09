package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Data
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "policyId")
    private Policy policy;

}
