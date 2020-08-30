package pl.nikowis.librin.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
