package pl.nikowis.librin.domain.rating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.librin.domain.rating.dto.CreateRatingDTO;
import pl.nikowis.librin.domain.rating.dto.RatingDTO;

public interface RatingService {

    Page<RatingDTO> getUserRatings(Long userId, Pageable pageable);

    RatingDTO createRating(Long userId, CreateRatingDTO dto);
}
