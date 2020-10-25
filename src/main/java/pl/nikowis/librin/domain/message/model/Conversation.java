package pl.nikowis.librin.domain.message.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.message.exception.ConversationNotFoundException;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Conversation extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    private User customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offerId")
    private Offer offer;

    @Transient
    private List<Message> messages = new ArrayList<>();

    private boolean offerOwnerRead;
    private boolean customerRead;
    private boolean empty;

    public String sendMessageToRecipient(Long currentUserId) {
        String recipientEmail = null;
        if (!isOfferOwner(currentUserId) && !isCustomer(currentUserId)) {
            throw new ConversationNotFoundException();
        }
        if (isCustomer(currentUserId)) {
            offerOwnerRead = false;
            customerRead = true;
            recipientEmail = offer.getOwner().getEmail().toString();
        } else {
            offerOwnerRead = true;
            customerRead = false;
            recipientEmail = customer.getEmail().toString();
        }

        updatedAt = new Date();
        empty = false;
        return recipientEmail;
    }

    public void markAsRead(Long userId) {
        if (isCustomer(userId)) {
            customerRead = true;
        } else {
            offerOwnerRead = true;
        }
    }

    @Transient
    @JsonIgnore
    private boolean isOfferOwner(Long userId) {
        return userId.equals(offer.getOwnerId());
    }

    @Transient
    @JsonIgnore
    private boolean isCustomer(Long userId) {
        return userId.equals(customer.getId());
    }
}
