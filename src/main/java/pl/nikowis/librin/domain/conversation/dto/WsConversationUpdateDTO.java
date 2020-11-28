package pl.nikowis.librin.domain.conversation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.nikowis.librin.domain.offer.model.OfferStatus;

import java.util.Date;

@Data
@NoArgsConstructor
public class WsConversationUpdateDTO {

    private String id;
    private Long conversationId;
    private String content;
    private Long createdBy;
    private Date createdAt;
    private OfferStatus offerStatus;
    private Boolean soldToMe;

    public WsConversationUpdateDTO(Long convId, Long ownerId, Long customerId, Long buyerId, OfferStatus status) {
        conversationId = convId;
        createdAt = new Date();
        createdBy = ownerId;
        offerStatus = status;
        soldToMe = OfferStatus.SOLD.equals(status) && customerId.equals(buyerId);
    }
}
