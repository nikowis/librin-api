package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Offer extends BaseEntity {

    private String title;
    private String author;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;

}
