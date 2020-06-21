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
import pl.nikowis.librin.dto.OfferDetailsDTO;
import pl.nikowis.librin.dto.OfferFilterDTO;
import pl.nikowis.librin.dto.OfferPreviewDTO;
import pl.nikowis.librin.exception.CannotBuyOwnOfferException;
import pl.nikowis.librin.exception.CannotUpdateOfferException;
import pl.nikowis.librin.exception.CustomerAccountBlockedException;
import pl.nikowis.librin.exception.CustomerAccountDeletedException;
import pl.nikowis.librin.exception.OfferCantBeUpdatedException;
import pl.nikowis.librin.exception.OfferDoesntExistException;
import pl.nikowis.librin.model.Attachment;
import pl.nikowis.librin.model.BaseEntity;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferSpecification;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.model.UserStatus;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.AttachmentService;
import pl.nikowis.librin.service.OfferService;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public Page<OfferPreviewDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        Page<Offer> offersPage = offerRepository.findAll(new OfferSpecification(filterDTO), pageable);
        offersPage.getContent().forEach(offer -> {
            List<Attachment> attachments = offer.getAttachments();
            if(attachments != null && attachments.size()> 0) {
                Attachment mainAttachment = offer.getAttachments().stream().filter(Attachment::isMain).findFirst().orElse(null);
                if(mainAttachment !=null) {
                    offer.setAttachment(attachmentService.fillAttachmentContent(mainAttachment));
                }
            }
        });
        return offersPage.map(o -> mapperFacade.map(o, OfferPreviewDTO.class));
    }

    @Override
    public OfferPreviewDTO createOffer(CreateOfferDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Offer offer = new Offer();
        offer.setStatus(OfferStatus.ACTIVE);
        mapperFacade.map(dto, offer);
        User currentUser = userRepository.findById(currentUserId).get();
        offer.setOwner(currentUser);
        offer = offerRepository.save(offer);
        if (dto.getPhotos() != null) {
            attachmentService.addAttachmentsToOffer(offer, dto.getPhotos());
        }
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO updateOffer(Long offerId, CreateOfferDTO offerDTO) {
        Offer offer = getOfferValidateOwner(offerId);
        validateOfferActive(offer);
        mapperFacade.map(offerDTO, offer);
        List<Attachment> oldAtts = offer.getAttachments();
        if (oldAtts != null) {
            attachmentService.removeOfferAttachments(oldAtts);
        }
        offer.setAttachments(null);
        offer = offerRepository.save(offer);
        if (offerDTO.getPhotos() != null) {
            attachmentService.addAttachmentsToOffer(offer, offerDTO.getPhotos());
        }
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO deleteOffer(Long offerDTO) {
        Offer offer = getOfferValidateOwner(offerDTO);
        validateOfferActive(offer);
        offer.setStatus(OfferStatus.DELETED);
        offer = offerRepository.save(offer);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    private void validateOfferActive(Offer offer) {
        if (!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new OfferCantBeUpdatedException();
        }
    }

    @Override
    public OfferDetailsDTO getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferDoesntExistException::new);
        List<Attachment> attachments = offer.getAttachments();
        if (attachments != null) {
            attachments = attachmentService.fillAttachmentContent(attachments);
            attachments.sort(Comparator.comparing(BaseEntity::getId));
            offer.setAttachments(attachments);
        }
        return mapperFacade.map(offer, OfferDetailsDTO.class);
    }

    @Override
    public OfferPreviewDTO offerSold(Long offerId, Long customerId) {
        Offer offer = getOfferValidateOwner(offerId);
        if(offer.getOwnerId().equals(customerId)) {
            throw new CannotBuyOwnOfferException();
        }
        Optional<User> customerOpt = userRepository.findById(customerId);
        User customer = customerOpt.orElseThrow(CannotUpdateOfferException::new);
        if(UserStatus.BLOCKED.equals(customer.getStatus())) {
            throw new CustomerAccountBlockedException();
        }
        if(UserStatus.DELETED.equals(customer.getStatus())) {
            throw new CustomerAccountDeletedException();
        }
        offer.setBuyer(customer);
        offer.setStatus(OfferStatus.SOLD);
        Offer saved = offerRepository.save(offer);
        Attachment attachment = saved.getAttachment();
        if (attachment != null) {
            Attachment att = attachmentService.fillAttachmentContent(attachment);
            offer.setAttachment(att);
        }
        return mapperFacade.map(saved, OfferPreviewDTO.class);
    }

    private Offer getOfferValidateOwner(Long offerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId());
        if (offer == null) {
            throw new OfferDoesntExistException();
        }
        return offer;
    }

}
