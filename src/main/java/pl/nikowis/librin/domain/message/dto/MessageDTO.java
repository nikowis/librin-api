package pl.nikowis.librin.domain.message.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {

    private String id;
    private String content;
    private Long createdBy;
    private Date createdAt;
    private Boolean read;
}
