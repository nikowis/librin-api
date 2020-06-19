package pl.nikowis.librin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationWithoutMessagesDTO {

    private Long id;
    private OfferPreviewDTO offer;
    private PublicUserDTO customer;
    private Date createdAt;
    private Date updatedAt;
    private boolean read;
}
