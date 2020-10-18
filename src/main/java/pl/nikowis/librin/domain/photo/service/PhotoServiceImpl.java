package pl.nikowis.librin.domain.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.PhotoRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PhotoFactory photoFactory;

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public List<Photo> saveAndStorePhotos(Offer offer, User owner, List<PhotoDTO> photosDto) {
        List<Photo> photos = photoRepository.saveAll(photoFactory.createPhotos(photosDto, owner, offer));
        fileStorageService.storeOfferPhotos(owner.getId(), offer.getId(), photosDto);
        return photos;
    }

    @Override
    public List<Photo> updatePhotos(Offer offer, List<PhotoDTO> newPhotos) {
        List<Photo> oldPhotos = offer.getPhotos();
        List<Photo> outputPhotos = new LinkedList<>();
        List<Photo> oldPhotosToRemove = oldPhotos.stream().filter(oldPhoto -> !isPresentInPhotoList(newPhotos, oldPhoto)).collect(Collectors.toList());
        List<Photo> oldPhotosToSave = oldPhotos.stream().filter(oldPhoto -> isPresentInPhotoList(newPhotos, oldPhoto)).collect(Collectors.toList());
        List<PhotoDTO> newPhotosToStore = newPhotos.stream().filter(photoDTO -> photoDTO.getUuid() == null).collect(Collectors.toList());
        User owner = offer.getOwner();
        List<Photo> newSavedPhotos = photoRepository.saveAll(photoFactory.createPhotos(newPhotosToStore, owner, offer));
        photoRepository.deleteAll(oldPhotosToRemove);
        fileStorageService.storeOfferPhotos(owner.getId(), offer.getId(), newPhotosToStore);
        fileStorageService.removeOfferPhotos(owner.getId(), offer.getId(), oldPhotosToRemove);
        outputPhotos.addAll(oldPhotosToSave);
        outputPhotos.addAll(newSavedPhotos);
        outputPhotos.sort(Comparator.comparing(Photo::getId));
        return outputPhotos;
    }

    private boolean isPresentInPhotoList(List<PhotoDTO> newPhotos, Photo photo) {
        return photo.getUuid() != null && newPhotos.stream().anyMatch(pDto -> pDto.getUuid() != null && pDto.getUuid().equals(photo.getUuid()));
    }


}
