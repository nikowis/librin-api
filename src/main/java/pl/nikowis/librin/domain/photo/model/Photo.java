package pl.nikowis.librin.domain.photo.model;

import lombok.Data;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.util.FilePathUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Photo extends BaseEntity {

    @NotNull
    private Long size;

    @NotBlank
    private String name;

    @Column(name = "ownerId", updatable = false, insertable = false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

    @Column(name = "offerId", updatable = false, insertable = false)
    private Long offerId;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private Offer offer;

    @Column(name="`order`")
    @Min(Offer.MIN_PHOTOS)
    @Max(Offer.MAX_PHOTOS)
    private Integer order;

    @Transient
    public String getPath() {
        return FilePathUtils.getOfferPhotoPath(ownerId, offerId, order, name);
    }
}
