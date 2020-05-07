package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Conversation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private User customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offerId")
    private Offer offer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "conversation", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Message> messages = new ArrayList<>();

}
