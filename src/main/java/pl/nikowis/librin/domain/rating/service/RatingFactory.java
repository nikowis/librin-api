package pl.nikowis.librin.domain.rating.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.rating.dto.CreateRatingDTO;
import pl.nikowis.librin.domain.rating.model.Rating;

@Component
public class RatingFactory {

    @Autowired
    private MapperFacade mapperFacade;

    public Rating createRating(CreateRatingDTO dto, Offer offer) {
        Rating r = mapperFacade.map(dto, Rating.class);
        r.setAuthor(offer.getBuyer());
        r.setUser(offer.getOwner());
        r.setOffer(offer);
        return r;
    }

}
