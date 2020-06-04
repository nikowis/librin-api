package pl.nikowis.librin.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Message extends BaseEntity {

    @NotBlank
    private String content;
    @NotNull
    private Long createdBy;

    @NotNull
    private boolean read;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

}
