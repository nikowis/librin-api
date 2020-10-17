package pl.nikowis.librin.domain.photo.service;

import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.photo.model.Photo;

import java.util.List;

public interface FileStorageService {

    void storeOfferPhotos(Long ownerId, Long offerId, List<PhotoDTO> photos);
    void removeOfferPhotos(Long ownerId, Long offerId, List<Photo> photos);

}
