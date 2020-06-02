package pl.nikowis.librin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Attachment extends BaseEntity {

    @NotNull
    private Long size;
    @NotBlank
    private String name;

    @Column(name = "ownerId", updatable = false, insertable = false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private Offer offer;

    @Transient
    @JsonInclude
    private String content;
}
