package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ConversationDTO {

    private Long id;
    private List<MessageDTO> messages = new ArrayList<>();
    private OfferDTO offer;
    private Date createdAt;
}
