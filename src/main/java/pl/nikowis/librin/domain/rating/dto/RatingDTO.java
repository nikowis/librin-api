package pl.nikowis.librin.domain.rating.dto;

import lombok.Data;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;

import java.util.Date;

@Data
public class RatingDTO {

   private Long id;
   private Short value;
   private PublicUserDTO author;
   private String description;
   private Date createdAt;
}
