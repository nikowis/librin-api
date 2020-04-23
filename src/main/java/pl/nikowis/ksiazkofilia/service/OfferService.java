package pl.nikowis.ksiazkofilia.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.ksiazkofilia.dto.CreateOfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferFilterDTO;

public interface OfferService {

    Page<OfferDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable);

    OfferDTO createOffer(CreateOfferDTO offer);

    OfferDTO updateOffer(Long offerId, CreateOfferDTO offerDTO);

    OfferDTO deleteOffer(Long offerDTO);

    OfferDTO getOffer(Long offerId);

    OfferDTO offerSold(Long offerId);
}
