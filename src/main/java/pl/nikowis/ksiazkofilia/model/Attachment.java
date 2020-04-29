package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
public class Attachment extends BaseEntity {

    @NotNull
    private Long size;
    @NotBlank
    private String name;
    @NotBlank
    private String content;

    @Column(name="ownerId", updatable=false, insertable=false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private Offer offer;
}
