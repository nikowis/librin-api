package pl.nikowis.librin.domain.offer.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.dto.OfferDetailsDTO;
import pl.nikowis.librin.domain.offer.dto.OfferFilterDTO;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;

public interface OfferService {

    Page<OfferPreviewDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable);

    OfferPreviewDTO createOffer(CreateOfferDTO offer);

    OfferPreviewDTO updateOffer(Long offerId, CreateOfferDTO offerDTO);

    OfferPreviewDTO deactivateOffer(Long offerDTO);

    OfferPreviewDTO activateOffer(Long offerDTO);

    OfferPreviewDTO deleteOffer(Long offerDTO);

    OfferDetailsDTO getOffer(Long offerId);

    OfferPreviewDTO offerSold(Long offerId, Long customerId);
}
