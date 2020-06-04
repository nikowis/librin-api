package pl.nikowis.librin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ConversationWithoutMessagesDTO {

    private Long id;
    private OfferDTO offer;
    private PublicUserDTO customer;
    private Date createdAt;
    private Date updatedAt;
    private boolean read;
}
