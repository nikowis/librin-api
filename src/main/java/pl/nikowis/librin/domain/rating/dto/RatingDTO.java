package pl.nikowis.librin.domain.rating.dto;

import lombok.Data;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;

@Data
public class RatingDTO {

   private Short value;
   private PublicUserDTO author;
   private String description;
}
