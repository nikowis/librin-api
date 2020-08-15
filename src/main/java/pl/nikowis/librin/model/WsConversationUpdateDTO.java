package pl.nikowis.librin.model;

import lombok.Data;

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
