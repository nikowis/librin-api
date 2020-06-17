package pl.nikowis.librin.rest;

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
import pl.nikowis.librin.dto.CreateOfferDTO;
import pl.nikowis.librin.dto.OfferDTO;
import pl.nikowis.librin.dto.OfferFilterDTO;
import pl.nikowis.librin.dto.SellOfferDTO;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.security.SecurityConstants;
import pl.nikowis.librin.service.OfferService;
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
    public static final String OFFER_SOLD_ENDPOINT = MY_OFFERS_ENDPOINT + SOLD_PATH;

    @Autowired
    private OfferService offerService;

    @GetMapping
    public Page<OfferDTO> offersList(OfferFilterDTO filterDTO, Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        filterDTO.setOwner(currentUserId);
        filterDTO.setStatuses(Lists.newArrayList(OfferStatus.ACTIVE, OfferStatus.SOLD));
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

    @PutMapping(path = SOLD_PATH)
    public OfferDTO offerSold(@PathVariable(OFFER_ID_VARIABLE) Long offerId,  @Validated @RequestBody SellOfferDTO dto) {
        return offerService.offerSold(offerId, dto.getCustomerId());
    }

    @DeleteMapping(path = OFFER_PATH)
    public OfferDTO deleteOffer(@PathVariable(OFFER_ID_VARIABLE) Long offerId) {
        return offerService.deleteOffer(offerId);
    }

}
