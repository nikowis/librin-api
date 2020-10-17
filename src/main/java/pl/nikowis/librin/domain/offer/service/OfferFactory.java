package pl.nikowis.librin.domain.offer.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.model.User;

import java.util.Collections;

@Component
public class OfferFactory {

    @Autowired
    private MapperFacade mapperFacade;

    public Offer createNewOffer(CreateOfferDTO dto, User owner) {
        Offer offer = new Offer();
        offer.setStatus(OfferStatus.ACTIVE);
        mapperFacade.map(dto, offer);
        offer.setOwner(owner);
        offer.setPhotos(Collections.emptyList());
        return offer;
    }

}
