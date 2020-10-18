package pl.nikowis.librin.domain.photo.service;

import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.domain.user.model.User;

import java.util.List;

public interface PhotoService {
    List<Photo> saveAndStorePhotos(Offer offer, User owner, List<PhotoDTO> photos);

    List<Photo> updatePhotos(Offer offer, List<PhotoDTO> newPhotos);

}
