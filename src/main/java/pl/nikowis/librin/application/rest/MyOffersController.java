package pl.nikowis.librin.application.rest;

import com.google.common.collect.Lists;
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
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.dto.OfferDetailsDTO;
import pl.nikowis.librin.domain.offer.dto.OfferFilterDTO;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.offer.dto.SellOfferDTO;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.offer.service.OfferService;
import pl.nikowis.librin.infrastructure.security.SecurityConstants;
import pl.nikowis.librin.util.SecurityUtils;


@RestController
@RequestMapping(path = MyOffersController.MY_OFFERS_ENDPOINT)
@Secured(SecurityConstants.ROLE_USER)
public class MyOffersController {

    public static final String MY_OFFERS_ENDPOINT = "/myoffers";
    public static final String OFFER_ID_VARIABLE = "offerId";
    public static final String OFFER_PATH = "/{" + OFFER_ID_VARIABLE + "}";
    public static final String OFFER_ENDPOINT = MY_OFFERS_ENDPOINT + OFFER_PATH;
    public static final String SOLD_PATH = OFFER_PATH + "/sold";
    public static final String DEACTIVATE_PATH = OFFER_PATH + "/deactivate";
    public static final String ACTIVATE_PATH = OFFER_PATH + "/activate";
    public static final String OFFER_SOLD_ENDPOINT = MY_OFFERS_ENDPOINT + SOLD_PATH;

    @Autowired
    private OfferService offerService;

    @GetMapping
    public Page<OfferPreviewDTO> offersList(OfferFilterDTO filterDTO, Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        filterDTO.setOwner(currentUserId);
        filterDTO.setStatuses(Lists.newArrayList(OfferStatus.ACTIVE, OfferStatus.SOLD, OfferStatus.INACTIVE));
        return offerService.getOffers(filterDTO, pageable);
    }

    @PostMapping
    public OfferPreviewDTO createOffer(@Validated @RequestBody CreateOfferDTO offer) {
        return offerService.createOffer(offer);
    }

    @GetMapping(path = OFFER_PATH)
    public OfferDetailsDTO getOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.getOffer(offerId);
    }

    @PutMapping(path = OFFER_PATH)
    public OfferPreviewDTO updateOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId, @Validated @RequestBody CreateOfferDTO offer) {
        return offerService.updateOffer(offerId, offer);
    }

    @PutMapping(path = SOLD_PATH)
    public OfferPreviewDTO offerSold(@PathVariable(OFFER_ID_VARIABLE) Long offerId, @Validated @RequestBody SellOfferDTO dto) {
        return offerService.offerSold(offerId, dto.getCustomerId());
    }

    @PutMapping(path = DEACTIVATE_PATH)
    public OfferPreviewDTO deactivateOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.deactivateOffer(offerId);
    }

    @PutMapping(path = ACTIVATE_PATH)
    public OfferPreviewDTO activateOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.activateOffer(offerId);
    }

    @DeleteMapping(path = OFFER_PATH)
    public OfferPreviewDTO deleteOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.deleteOffer(offerId);
    }

}
