package pl.nikowis.librin.domain.rating.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.rating.dto.CreateRatingDTO;
import pl.nikowis.librin.domain.rating.dto.RatingDTO;
import pl.nikowis.librin.domain.rating.exception.UnauthorizedCreateRatingException;
import pl.nikowis.librin.domain.rating.model.Rating;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.RatingRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
import pl.nikowis.librin.infrastructure.security.SecurityConstants;
import pl.nikowis.librin.util.SecurityUtils;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingFactory ratingFactory;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public Page<RatingDTO> getUserRatings(Long userId, Pageable pageable) {
        return ratingRepository.findByUserId(userId, pageable).map(rating -> mapperFacade.map(rating, RatingDTO.class));
    }

    @Override
    @Secured(SecurityConstants.ROLE_USER)
    @Transactional
    public RatingDTO createRating(Long userId, CreateRatingDTO dto) {
        Offer offer = offerRepository.findById(dto.getOfferId()).orElseThrow(UnauthorizedCreateRatingException::new);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        offer.validateCanCreateRating(userId, currentUserId);

        Rating saved = ratingRepository.saveAndFlush(ratingFactory.createRating(dto, offer));

        userRepository.updateUserRating(userId);

        return mapperFacade.map(saved, RatingDTO.class);
    }
}
