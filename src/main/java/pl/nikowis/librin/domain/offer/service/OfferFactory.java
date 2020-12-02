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
import pl.nikowis.librin.domain.user.dto.CityDTO;
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
        City city = getPickupCity(dto, offer);
        dto.setSelfPickupCity(null);
        mapperFacade.map(dto, offer);
        offer.setSelfPickupCity(city);
        offer.setOwner(owner);
        offer.setPhotos(Collections.emptyList());
        return offer;
    }

    public City getPickupCity(CreateOfferDTO dto, Offer offer) {
        City city = null;
        CityDTO dtoCity = dto.getSelfPickupCity();
        if(Boolean.TRUE.equals(dto.getSelfPickup())) {
            if(dtoCity == null || dtoCity.getId() == null) {
                throw new IncorrectSelfPickupCityException();
            }
            city = cityRepository.findById(dtoCity.getId()).orElseThrow(IncorrectSelfPickupCityException::new);
        } else {
            offer.setSelfPickupCity(null);
        }
        if (Boolean.FALSE.equals(dto.getShipment()) && Boolean.FALSE.equals(dto.getSelfPickup())) {
            throw new NoShipmentMethodChosenException();
        }
        //dont map city into user's city
        dto.setSelfPickupCity(null);
        return city;
    }

}
