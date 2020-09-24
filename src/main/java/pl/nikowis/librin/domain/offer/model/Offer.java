package pl.nikowis.librin.domain.offer.model;

import lombok.Data;
import pl.nikowis.librin.domain.attachment.model.Attachment;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Offer extends BaseEntity {

    private String title;
    private String author;
    private String description;

    @Column(name = "ownerId", updatable = false, insertable = false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "buyerId")
    private User buyer;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Enumerated(EnumType.STRING)
    private OfferCategory category;

    @Enumerated(EnumType.ORDINAL)
    private OfferCondition condition;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer", cascade = {CascadeType.ALL})
    private List<Attachment> attachments = new ArrayList<>();

    @Transient
    private Attachment attachment;
}
