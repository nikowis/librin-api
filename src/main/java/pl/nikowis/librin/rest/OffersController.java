package pl.nikowis.librin.rest;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.dto.OfferDetailsDTO;
import pl.nikowis.librin.dto.OfferPreviewDTO;
import pl.nikowis.librin.dto.OfferFilterDTO;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.service.OfferService;


@RestController
@RequestMapping(path = OffersController.OFFERS_ENDPOINT)
public class OffersController {

    public static final String OFFERS_ENDPOINT = "/offers";
    public static final String OFFER_ID_VARIABLE = "offerId";
    public static final String OFFER_PATH = "/{" + OFFER_ID_VARIABLE + "}";
    public static final String OFFER_ENDPOINT = OFFERS_ENDPOINT + OFFER_PATH;

    @Autowired
    private OfferService offerService;

    @GetMapping
    public Page<OfferPreviewDTO> offersList(OfferFilterDTO filterDTO, Pageable pageable) {
        filterDTO.setStatuses(Lists.newArrayList(OfferStatus.ACTIVE));
        return offerService.getOffers(filterDTO, pageable);
    }

    @GetMapping(path = OFFER_PATH)
    public OfferDetailsDTO getOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.getOffer(offerId);
    }

}
