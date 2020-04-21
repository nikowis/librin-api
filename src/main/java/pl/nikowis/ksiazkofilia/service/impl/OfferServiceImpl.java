package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.nikowis.ksiazkofilia.dto.CreateOfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferFilterDTO;
import pl.nikowis.ksiazkofilia.exception.OfferDoesntExistException;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.model.OfferSpecification;
import pl.nikowis.ksiazkofilia.model.UserDetailsImpl;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.service.OfferService;
import pl.nikowis.ksiazkofilia.util.SecurityUtils;

@Service
@Transactional
class OfferServiceImpl implements OfferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public Page<OfferDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        return offerRepository.findAll(new OfferSpecification(filterDTO), pageable).map(g -> mapperFacade.map(g, OfferDTO.class));
    }

    @Override
    public OfferDTO createOffer(CreateOfferDTO dto) {
        UserDetailsImpl currentUserDetails = SecurityUtils.getCurrentUser();

        Offer offer = new Offer();
        mapperFacade.map(dto, offer);
        offer.setOwner(userRepository.findById(currentUserDetails.getId()).get());
        offer = offerRepository.save(offer);
        return mapperFacade.map(offer, OfferDTO.class);
    }

    @Override
    public OfferDTO updateOffer(Long offerId, CreateOfferDTO offerDTO) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId());
        if(offer == null) {
            throw new OfferDoesntExistException();
        }
        mapperFacade.map(offerDTO, offer);
        Offer saved = offerRepository.save(offer);
        return mapperFacade.map(saved, OfferDTO.class);
    }

    @Override
    public OfferDTO deleteOffer(Long offerDTO) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerDTO, SecurityUtils.getCurrentUserId());
        if(offer == null) {
            throw new OfferDoesntExistException();
        }
        OfferDTO deletedDTO = mapperFacade.map(offer, OfferDTO.class);
        offerRepository.delete(offer);
        return deletedDTO;
    }

    @Override
    public OfferDTO getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferDoesntExistException::new);
        return mapperFacade.map(offer, OfferDTO.class);
    }

}
