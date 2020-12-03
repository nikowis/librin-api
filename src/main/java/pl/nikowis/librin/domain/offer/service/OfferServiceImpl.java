package pl.nikowis.librin.domain.offer.service;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.conversation.service.ConversationServiceImpl;
import pl.nikowis.librin.domain.offer.dto.CreateOfferDTO;
import pl.nikowis.librin.domain.offer.dto.OfferDetailsDTO;
import pl.nikowis.librin.domain.offer.dto.OfferFilterDTO;
import pl.nikowis.librin.domain.offer.dto.OfferPreviewDTO;
import pl.nikowis.librin.domain.offer.exception.CannotUpdateOfferException;
import pl.nikowis.librin.domain.offer.exception.OfferDoesntExistException;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.domain.photo.service.PhotoService;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.OfferSpecification;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferFactory offerFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ConversationServiceImpl messageService;

    @Autowired
    private PhotoService photoService;

    @Override
    public Page<OfferPreviewDTO> getOffers(OfferFilterDTO filterDTO, Pageable pageable) {
        Page<Offer> offersPage = offerRepository.findAll(new OfferSpecification(filterDTO), pageable);
        offersPage.getContent().forEach(offer -> {
            List<Photo> photos = offer.getPhotos();
            if (photos != null && photos.size() > 0) {
                offer.getPhotos().sort(Comparator.comparing(Photo::getId));
                offer.setPhoto(offer.getPhotos().get(0));
            }
        });
        return offersPage.map(o -> mapperFacade.map(o, OfferPreviewDTO.class));
    }

    @Override
    public OfferPreviewDTO createOffer(CreateOfferDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User owner = userRepository.findById(currentUserId).get();
        Offer offer = offerFactory.createNewOffer(dto, owner);
        offer = offerRepository.save(offer);
        offer.setPhotos(photoService.saveAndStorePhotos(offer, owner, dto.getPhotos()));
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO updateOffer(Long offerId, CreateOfferDTO offerDTO) {
        Long ownerId = SecurityUtils.getCurrentUserId();
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, ownerId).orElseThrow(OfferDoesntExistException::new);
        offer.validateUpdateOffer();
        List<Photo> updatedPhotos = photoService.updatePhotos(offer, offerDTO.getPhotos());
        mapperFacade.map(offerDTO, offer);
        offer.setPhotos(updatedPhotos);
        offer.setPhoto(updatedPhotos.size() > 0 ? updatedPhotos.get(0) : null);
        offer = offerRepository.save(offer);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO deactivateOffer(Long offerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId()).orElseThrow(OfferDoesntExistException::new);
        offer.deactivateOffer();
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerId, OfferStatus.INACTIVE, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO activateOffer(Long offerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId()).orElseThrow(OfferDoesntExistException::new);
        offer.activateOffer();
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerId, OfferStatus.ACTIVE, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferPreviewDTO deleteOffer(Long offerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId()).orElseThrow(OfferDoesntExistException::new);
        offer.deleteOffer();
        offer = offerRepository.save(offer);
        messageService.notifyAllConversationsOfferStatusChange(offerId, OfferStatus.DELETED, null);
        return mapperFacade.map(offer, OfferPreviewDTO.class);
    }

    @Override
    public OfferDetailsDTO getOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferDoesntExistException::new);
        offer.validateViewDetails();
        List<Photo> photos = offer.getPhotos();
        if (photos != null) {
            photos.sort(Comparator.comparing(Photo::getId));
            offer.setPhotos(photos);
        }
        return mapperFacade.map(offer, OfferDetailsDTO.class);
    }

    @Override
    public OfferPreviewDTO offerSold(Long offerId, Long customerId) {
        Offer offer = offerRepository.findByIdAndOwnerId(offerId, SecurityUtils.getCurrentUserId()).orElseThrow(OfferDoesntExistException::new);
        User customer = userRepository.findById(customerId).orElseThrow(CannotUpdateOfferException::new);
        offer.sellOffer(customer);

        Offer saved = offerRepository.save(offer);

        messageService.notifyAllConversationsOfferStatusChange(offerId, OfferStatus.SOLD, customerId);

        return mapperFacade.map(saved, OfferPreviewDTO.class);
    }

}
