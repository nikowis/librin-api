package pl.nikowis.librin.domain.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Conversation extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    private User customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offerId")
    private Offer offer;

    @Transient
    private List<Message> messages = new ArrayList<>();

    private boolean offerOwnerRead;
    private boolean customerRead;

    @Transient
    @JsonInclude
    private Boolean read;

    @Override
    public void preUpdate() {

    }
}
