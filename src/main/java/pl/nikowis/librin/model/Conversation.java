package pl.nikowis.librin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
