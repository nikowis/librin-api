package pl.nikowis.librin.config;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import pl.nikowis.librin.dto.OfferPreviewDTO;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.util.SecurityUtils;

public class OrikaMapperConfig {

    public static MapperFacade getMapperFacade() {
        return configure().getMapperFacade();
    }

    private static MapperFactory configure() {
        MapperFactory mapperFactory = MapperConfiguration.mapperFactory();
        mapperFactory.classMap(Offer.class, OfferPreviewDTO.class)
                .byDefault()
                .customize(
                        new CustomMapper<Offer, OfferPreviewDTO>() {
                            public void mapAtoB(Offer o, OfferPreviewDTO dto, MappingContext context) {
                                if (OfferStatus.SOLD.equals(o.getStatus()) && o.getBuyer() != null) {
                                    Long currentUserId = SecurityUtils.getCurrentUserId();
                                    dto.setSoldToMe(o.getBuyer().getId().equals(currentUserId));
                                    dto.setBuyerId(o.getOwnerId().equals(currentUserId) ? o.getBuyer().getId() : null);
                                }
                            }
                        }
                )
                .register();
        return mapperFactory;
    }


}