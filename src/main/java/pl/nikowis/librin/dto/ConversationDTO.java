package pl.nikowis.librin.dto;

import lombok.Data;

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
