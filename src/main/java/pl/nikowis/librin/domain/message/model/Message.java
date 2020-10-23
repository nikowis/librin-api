package pl.nikowis.librin.domain.message.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    protected String id;
    protected Date createdAt;
    @LastModifiedDate
    protected Date updatedAt;
    @NotBlank
    private String content;
    @NotNull
    private Long createdBy;
    private boolean read;
    @NotNull
    private Long conversationId;

}
