package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {

    private Long id;
    private String content;
    private Long createdBy;
    private Date createdAt;
}
