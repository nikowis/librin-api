package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Data
public class Offer extends BaseEntity {
    private String title;
    private String author;

    @Column(name="ownerId", updatable=false, insertable=false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;
}
