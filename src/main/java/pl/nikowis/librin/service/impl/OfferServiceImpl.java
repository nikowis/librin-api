package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.CreateOfferDTO;
import pl.nikowis.librin.dto.OfferDTO;
import pl.nikowis.librin.dto.OfferFilterDTO;
import pl.nikowis.librin.exception.OfferCantBeUpdatedException;
import pl.nikowis.librin.exception.OfferDoesntExistException;
import pl.nikowis.librin.model.Attachment;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferSpecification;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.AttachmentService;
import pl.nikowis.librin.service.OfferService;
import pl.nikowis.librin.util.SecurityUtils;

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
    private AttachmentService attachmentService;


    @Override
    public Page<OfferDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        Page<Offer> offersPage = offerRepository.findAll(new OfferSpecification(filterDTO), pageable);
        offersPage.getContent().forEach(offer -> {
            Attachment attachment = offer.getAttachment();
            if (attachment != null) {
                Attachment att = attachmentService.fillAttachmentContent(attachment);
                offer.setAttachment(att);
            }
        });
        return offersPage.map(o -> mapperFacade.map(o, OfferDTO.class));
    }

    @Override
    public OfferDTO createOffer(CreateOfferDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Offer offer = new Offer();
        offer.setStatus(OfferStatus.ACTIVE);
        mapperFacade.map(dto, offer);
        User currentUser = userRepository.findById(currentUserId).get();
        offer.setOwner(currentUser);
        offer = offerRepository.save(offer);
        if (dto.getPhoto() != null) {
            Attachment attachment = attachmentService.addAttachmentToOffer(offer, dto.getPhoto());
            offer.setAttachment(attachment);
        }
        return mapperFacade.map(offer, OfferDTO.class);
    }

    @Override
    public OfferDTO updateOffer(Long offerId, CreateOfferDTO offerDTO) {
        Offer offer = getOfferValidateOwner(offerId);
        validateOfferActive(offer);
        mapperFacade.map(offerDTO, offer);
        Attachment oldAtt = offer.getAttachment();
        if (oldAtt != null) {
            attachmentService.removeOfferAttachment(oldAtt);
        }
        Offer saved = offerRepository.save(offer);
        if (offerDTO.getPhoto() != null) {
            Attachment attachment = attachmentService.addAttachmentToOffer(offer, offerDTO.getPhoto());
            offer.setAttachment(attachment);
        }
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
        Attachment attachment = offer.getAttachment();
        if (attachment != null) {
            Attachment att = attachmentService.fillAttachmentContent(attachment);
            offer.setAttachment(att);
        }
        return mapperFacade.map(offer, OfferDTO.class);
    }

    @Override
    public OfferDTO offerSold(Long offerId) {
        Offer offer = getOfferValidateOwner(offerId);
        offer.setStatus(OfferStatus.SOLD);
        Offer saved = offerRepository.save(offer);
        Attachment attachment = saved.getAttachment();
        if (attachment != null) {
            Attachment att = attachmentService.fillAttachmentContent(attachment);
            offer.setAttachment(att);
        }
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
