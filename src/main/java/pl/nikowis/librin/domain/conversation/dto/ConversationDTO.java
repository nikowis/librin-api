package pl.nikowis.librin.domain.conversation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Date;

@Data
public class ConversationDTO {

    protected  Long id;
    protected OfferPreviewDTO offer;
    protected PublicUserDTO customer;
    protected Date createdAt;
    protected  Date updatedAt;

    @JsonIgnore
    protected  boolean customerRead;
    @JsonIgnore
    protected  boolean offerOwnerRead;

    @JsonInclude
    public Boolean getRead() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if(currentUserId == null) {
            return null;
        } else if (currentUserId.equals(customer.getId())) {
            return customerRead;
        } else {
            return offerOwnerRead;
        }
    }

}
