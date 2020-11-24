package pl.nikowis.librin.domain.rating.model;

import lombok.Data;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Rating extends BaseEntity {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;

    private String description;

}
