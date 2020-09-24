package pl.nikowis.librin.domain.message.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.model.OfferStatus;

import java.util.Date;

@Data
public class WsConversationUpdateDTO {

    private String id;
    private Long conversationId;
    private String content;
    private Long createdBy;
    private Date createdAt;
    private OfferStatus offerStatus;
    private Boolean soldToMe;

}
