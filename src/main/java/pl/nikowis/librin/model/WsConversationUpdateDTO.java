package pl.nikowis.librin.model;

import lombok.Data;

import java.util.Date;

@Data
public class WsConversationUpdateDTO {

    private Long id;
    private Long conversationId;
    private String content;
    private Long createdBy;
    private Date createdAt;

}
