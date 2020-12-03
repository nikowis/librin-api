package pl.nikowis.librin.domain.offer.model;

import lombok.Data;
import pl.nikowis.librin.domain.city.model.City;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.exception.CannotBuyOwnOfferException;
import pl.nikowis.librin.domain.offer.exception.OfferCantBeUpdatedException;
import pl.nikowis.librin.domain.offer.exception.OfferDoesntExistException;
import pl.nikowis.librin.domain.offer.exception.OfferIsSoldException;
import pl.nikowis.librin.domain.rating.exception.UnauthorizedCreateRatingException;
import pl.nikowis.librin.domain.rating.model.Rating;
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
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Offer extends BaseEntity {

    public static final int MIN_PHOTOS = 1;
    public static final int MAX_PHOTOS = 3;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer")
    private List<Photo> photos = new ArrayList<>();

    @Transient
    private Photo photo;

    @OneToOne(mappedBy = "offer", fetch = FetchType.EAGER)
    private Rating rating;

    private Boolean exchange;

    public void deleteOffer() {
        if (OfferStatus.SOLD.equals(status) || OfferStatus.DELETED.equals(status)) {
            throw new OfferCantBeUpdatedException();
        }
        status = OfferStatus.DELETED;
    }


    public void validateUpdateOffer() {
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

    public void validateCanCreateRating(Long offerOwnerId, Long ratingAuthorId) {
        if(!OfferStatus.SOLD.equals(status) || rating != null || !isUserTheBuyer(ratingAuthorId) || !ownerId.equals(offerOwnerId)) {
            throw new UnauthorizedCreateRatingException();
        }
    }

    private boolean isUserTheBuyer(Long ratingAuthorId) {
        return buyer != null && ratingAuthorId.equals(buyer.getId());
    }
}
