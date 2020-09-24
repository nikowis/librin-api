package pl.nikowis.librin.infrastructure.config;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import pl.nikowis.librin.domain.offer.dto.BaseOfferDTO;
import pl.nikowis.librin.domain.offer.dto.OfferDetailsDTO;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.util.SecurityUtils;

public class OrikaMapperConfig {

    private static void mapOfferToOfferDto(Offer o, BaseOfferDTO dto) {
        if (OfferStatus.SOLD.equals(o.getStatus()) && o.getBuyer() != null) {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            dto.setSoldToMe(o.getBuyer().getId().equals(currentUserId));
            dto.setBuyerId(o.getOwnerId().equals(currentUserId) ? o.getBuyer().getId() : null);
        }
    }

    public static MapperFacade getMapperFacade() {
        return configure().getMapperFacade();
    }

    private static MapperFactory configure() {
        MapperFactory mapperFactory = MapperConfiguration.mapperFactory();
        mapperFactory.classMap(Offer.class, OfferPreviewDTO.class)
                .byDefault()
                .customize(new CustomMapper<Offer, OfferPreviewDTO>() {
                    public void mapAtoB(Offer o, OfferPreviewDTO dto, MappingContext context) {
                        mapOfferToOfferDto(o, dto);
                    }
                })
                .register();
        mapperFactory.classMap(Offer.class, OfferDetailsDTO.class)
                .byDefault()
                .customize(new CustomMapper<Offer, OfferDetailsDTO>() {
                    @Override
                    public void mapAtoB(Offer offer, OfferDetailsDTO offerDetailsDTO, MappingContext context) {
                        mapOfferToOfferDto(offer, offerDetailsDTO);
                    }
                })
                .register();
        return mapperFactory;
    }


}