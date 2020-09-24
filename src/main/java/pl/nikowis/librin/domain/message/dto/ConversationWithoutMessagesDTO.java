package pl.nikowis.librin.domain.message.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;

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
