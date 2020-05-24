package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;
import pl.nikowis.ksiazkofilia.dto.CreateOfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferDTO;
import pl.nikowis.ksiazkofilia.dto.OfferFilterDTO;
import pl.nikowis.ksiazkofilia.exception.OfferCantBeUpdatedException;
import pl.nikowis.ksiazkofilia.exception.OfferDoesntExistException;
import pl.nikowis.ksiazkofilia.model.Attachment;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.model.OfferSpecification;
import pl.nikowis.ksiazkofilia.model.OfferStatus;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.repository.AttachmentRepository;
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

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public Page<OfferDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        return offerRepository.findAll(new OfferSpecification(filterDTO), pageable).map(g -> mapperFacade.map(g, OfferDTO.class));
    }

    @Override
    public OfferDTO createOffer(CreateOfferDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Offer offer = new Offer();
        offer.setStatus(OfferStatus.ACTIVE);
        mapperFacade.map(dto, offer);
        User currentUser = userRepository.findById(currentUserId).get();
        offer.setOwner(currentUser);

        createAndAddAttachment(dto, offer, currentUser);

        offer = offerRepository.save(offer);
        return mapperFacade.map(offer, OfferDTO.class);
    }

    private void createAndAddAttachment(CreateOfferDTO dto, Offer offer, User user) {
        AttachmentDTO file = dto.getPhoto();
        if (file != null) {
            Attachment attachment = new Attachment();
            attachment.setName(file.getName());
            attachment.setContent(file.getContent());
            attachment.setOffer(offer);
            attachment.setOwner(user);
            attachment.setOwnerId(user.getId());
            attachment.setSize((long) file.getContent().getBytes().length);
            offer.setAttachment(attachment);
        }
    }

    @Override
    public OfferDTO updateOffer(Long offerId, CreateOfferDTO offerDTO) {
        Offer offer = getOfferValidateOwner(offerId);
        validateOfferActive(offer);
        mapperFacade.map(offerDTO, offer);
        Attachment oldAtt = offer.getAttachment();
        if (oldAtt != null) {
            attachmentRepository.delete(oldAtt);
        }
        createAndAddAttachment(offerDTO, offer, offer.getOwner());
        Offer saved = offerRepository.save(offer);
        return mapperFacade.map(saved, OfferDTO.class);
    }

    @Override
    public OfferDTO deleteOffer(Long offerDTO) {
        Offer offer = getOfferValidateOwner(offerDTO);
        validateOfferActive(offer);
        offer.setStatus(OfferStatus.DELETED);
        offer = offerRepository.save(offer);
        return mapperFacade.map(offer, OfferDTO.class);
    }

    private void validateOfferActive(Offer offer) {
        if (!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new OfferCantBeUpdatedException();
        }
    }

    @Override
    public OfferDTO getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferDoesntExistException::new);
        return mapperFacade.map(offer, OfferDTO.class);
    }

    @Override
    public OfferDTO offerSold(Long offerId) {
        Offer offer = getOfferValidateOwner(offerId);
        offer.setStatus(OfferStatus.SOLD);
        Offer saved = offerRepository.save(offer);
        return mapperFacade.map(saved, OfferDTO.class);
    }

    private Offer getOfferValidateOwner(Long offerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId());
        if (offer == null) {
            throw new OfferDoesntExistException();
        }
        return offer;
    }

}
