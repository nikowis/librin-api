package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.dto.CreateOfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferFilterDTO;
import pl.nikowis.ksiazkofilia.security.SecurityConstants;
import pl.nikowis.ksiazkofilia.service.OfferService;


@RestController
@RequestMapping(path = OfferController.OFFERS_ENDPOINT)
@Secured(SecurityConstants.ROLE_USER)
public class OfferController {

    public static final String OFFERS_ENDPOINT = "/offers";
    public static final String OFFER_ID_VARIABLE = "offerId";
    public static final String OFFER_PATH = "/{" + OFFER_ID_VARIABLE + "}";
    public static final String OFFER_ENDPOINT = OFFERS_ENDPOINT + OFFER_PATH;

    @Autowired
    private OfferService offerService;

    @GetMapping
    public Page<OfferDTO> offersList(OfferFilterDTO filterDTO, Pageable pageable) {
        return offerService.getOffers(filterDTO, pageable);
    }

    @PostMapping
    public OfferDTO createOffer(@Validated @RequestBody CreateOfferDTO offer) {
        return offerService.createOffer(offer);
    }

    @GetMapping(path = OFFER_PATH)
    public OfferDTO getOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.getOffer(offerId);
    }

    @PutMapping(path = OFFER_PATH)
    public OfferDTO updateOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId, @Validated @RequestBody CreateOfferDTO offer) {
        return offerService.updateOffer(offerId, offer);
    }

    @DeleteMapping(path = OFFER_PATH)
    public OfferDTO deleteOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.deleteOffer(offerId);
    }

}
