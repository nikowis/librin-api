package pl.nikowis.librin.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Book {

    @Id
    protected String id;
    protected String author;
    protected String title;
    protected String subtitle;
    protected String datestamp;

}
