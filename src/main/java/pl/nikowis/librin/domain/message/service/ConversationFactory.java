package pl.nikowis.librin.domain.message.service;

import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.message.exception.CantCreateConversationOnNonActiveOfferException;
import pl.nikowis.librin.domain.message.model.Conversation;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.model.User;

@Component
public class ConversationFactory {

    Conversation createConversation(User customer, Offer offer) {
        if (!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new CantCreateConversationOnNonActiveOfferException();
        }
        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setOffer(offer);
        conversation.setCustomerRead(true);
        conversation.setOfferOwnerRead(true);
        conversation.setEmpty(true);
        return conversation;
    }
}
