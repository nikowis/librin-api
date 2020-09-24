package pl.nikowis.librin.domain.offer.service;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.attachment.model.Attachment;
import pl.nikowis.librin.domain.attachment.service.AttachmentService;
import pl.nikowis.librin.domain.base.BaseEntity;
import pl.nikowis.librin.domain.message.service.MessageServiceImpl;
import pl.nikowis.librin.domain.offer.dto.CannotBuyOwnOfferException;
import pl.nikowis.librin.domain.offer.dto.CannotUpdateOfferException;
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.dto.OfferCantBeUpdatedException;
import pl.nikowis.librin.domain.offer.dto.OfferDetailsDTO;
import pl.nikowis.librin.domain.offer.dto.OfferDoesntExistException;
import pl.nikowis.librin.domain.offer.dto.OfferFilterDTO;
import pl.nikowis.librin.domain.offer.dto.OfferIsSoldException;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.user.dto.CustomerAccountBlockedException;
import pl.nikowis.librin.domain.user.dto.CustomerAccountDeletedException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.OfferSpecification;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private MessageServiceImpl messageService;

    @Override
    public Page<OfferPreviewDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        Page<Offer> offersPage = offerRepository.findAll(new OfferSpecification(filterDTO), pageable);
        offersPage.getContent().forEach(offer -> {
            List<Attachment> attachments = offer.getAttachments();
            if (attachments != null && attachments.size() > 0) {
                Attachment mainAttachment = offer.getAttachments().stream().filter(Attachment::isMain).findFirst().orElse(null);
                if (mainAttachment != null) {
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
    public OfferPreviewDTO deactivateOffer(Long offerDTO) {
        Offer offer = getOfferValidateOwner(offerDTO);
        if (!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new OfferCantBeUpdatedException();
        }
        offer.setStatus(OfferStatus.INACTIVE);
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerDTO, OfferStatus.INACTIVE, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO activateOffer(Long offerDTO) {
        Offer offer = getOfferValidateOwner(offerDTO);
        if (!OfferStatus.INACTIVE.equals(offer.getStatus())) {
            throw new OfferCantBeUpdatedException();
        }
        offer.setStatus(OfferStatus.ACTIVE);
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerDTO, OfferStatus.ACTIVE, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO deleteOffer(Long offerDTO) {
        Offer offer = getOfferValidateOwner(offerDTO);
        validateOfferActive(offer);
        offer.setStatus(OfferStatus.DELETED);
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerDTO, OfferStatus.DELETED, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    private void validateOfferActive(Offer offer) {
        if (OfferStatus.SOLD.equals(offer.getStatus())) {
            throw new OfferIsSoldException();
        }
        if (OfferStatus.DELETED.equals(offer.getStatus())) {
            throw new OfferCantBeUpdatedException();
        }
    }

    @Override
    public OfferDetailsDTO getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferDoesntExistException::new);
        if (OfferStatus.DELETED.equals(offer.getStatus())) {
            throw new OfferDoesntExistException();
        }
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
        if (offer.getOwnerId().equals(customerId)) {
            throw new CannotBuyOwnOfferException();
        }
        Optional<User> customerOpt = userRepository.findById(customerId);
        User customer = customerOpt.orElseThrow(CannotUpdateOfferException::new);
        if (UserStatus.BLOCKED.equals(customer.getStatus())) {
            throw new CustomerAccountBlockedException();
        }
        if (UserStatus.DELETED.equals(customer.getStatus())) {
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

        messageService.notifyAllConversationsOfferStatusChange(offerId, OfferStatus.SOLD, customerId);

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
