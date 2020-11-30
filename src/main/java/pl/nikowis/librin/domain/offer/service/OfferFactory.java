package pl.nikowis.librin.domain.offer.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.city.model.City;
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.exception.IncorrectSelfPickupCityException;
import pl.nikowis.librin.domain.offer.exception.NoShipmentMethodChosenException;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.CityRepository;

import java.util.Collections;

@Component
public class OfferFactory {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private CityRepository cityRepository;

    public Offer createNewOffer(CreateOfferDTO dto, User owner) {
        Offer offer = new Offer();
        offer.setStatus(OfferStatus.ACTIVE);
        mapperFacade.map(dto, offer);

        if(Boolean.TRUE.equals(dto.getSelfPickup())) {
            if(dto.getSelfPickupCity() == null) {
                throw new IncorrectSelfPickupCityException();
            }
            City city = cityRepository.findById(dto.getSelfPickupCity().getId()).orElseThrow(IncorrectSelfPickupCityException::new);
            offer.setSelfPickupCity(city);
        }

        if (Boolean.FALSE.equals(dto.getShipment()) && Boolean.FALSE.equals(dto.getSelfPickup())) {
            throw new NoShipmentMethodChosenException();
        }

        offer.setOwner(owner);
        offer.setPhotos(Collections.emptyList());
        return offer;
    }

}
