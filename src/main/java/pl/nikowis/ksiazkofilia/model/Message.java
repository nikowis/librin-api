package pl.nikowis.ksiazkofilia.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Data
public class Message extends BaseEntity {

    @NotBlank
    private String content;
    @NotNull
    private Long createdBy;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

}
