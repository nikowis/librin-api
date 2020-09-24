package pl.nikowis.librin.domain.message.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ConversationDTO {

    private Long id;
    private List<MessageDTO> messages = new ArrayList<>();
    private OfferPreviewDTO offer;
    private PublicUserDTO customer;
    private Date createdAt;
    private Date updatedAt;
    private boolean read;
}
