package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@Data
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;
    protected Boolean active = Boolean.TRUE;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
