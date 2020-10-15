package pl.nikowis.librin.domain.offer.model;

import lombok.Data;
import pl.nikowis.librin.domain.attachment.model.Attachment;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.exception.CannotBuyOwnOfferException;
import pl.nikowis.librin.domain.offer.exception.OfferCantBeUpdatedException;
import pl.nikowis.librin.domain.offer.exception.OfferDoesntExistException;
import pl.nikowis.librin.domain.offer.exception.OfferIsSoldException;
import pl.nikowis.librin.domain.user.exception.CustomerAccountBlockedException;
import pl.nikowis.librin.domain.user.exception.CustomerAccountDeletedException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;

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

    public void deleteOffer() {
        if (OfferStatus.SOLD.equals(status) || OfferStatus.DELETED.equals(status)) {
            throw new OfferCantBeUpdatedException();
        }
        status = OfferStatus.DELETED;
    }


    public void updateOffer() {
        if (OfferStatus.SOLD.equals(status)) {
            throw new OfferIsSoldException();
        }
        if (OfferStatus.DELETED.equals(status)) {
            throw new OfferCantBeUpdatedException();
        }
    };

    public void deactivateOffer() {
        if (!OfferStatus.ACTIVE.equals(status)) {
            throw new OfferCantBeUpdatedException();
        }
        status = OfferStatus.INACTIVE;
    }

    public void activateOffer() {
        if (!OfferStatus.INACTIVE.equals(status)) {
            throw new OfferCantBeUpdatedException();
        }
        status = OfferStatus.ACTIVE;
    }

    public void validateViewDetails() {
        if (OfferStatus.DELETED.equals(status)) {
            throw new OfferDoesntExistException();
        }
    }

    public void sellOffer(User customer) {
        if (ownerId.equals(customer.getId())) {
            throw new CannotBuyOwnOfferException();
        }
        if (UserStatus.BLOCKED.equals(customer.getStatus())) {
            throw new CustomerAccountBlockedException();
        }
        if (UserStatus.DELETED.equals(customer.getStatus())) {
            throw new CustomerAccountDeletedException();
        }

        buyer = customer;
        status = OfferStatus.SOLD;
    }
}
