package pl.nikowis.librin.domain.photo.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.domain.user.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
public class PhotoFactory {

    @Autowired
    private MapperFacade mapperFacade;

    public Photo createPhoto(PhotoDTO photoDTO) {
        Photo photo = new Photo();
        mapperFacade.map(photoDTO, photo);
        return photo;
    }

    public List<Photo> createPhotos(List<PhotoDTO> photosDTO, User owner, Offer offer) {
        List<Photo> photos = new LinkedList<>();
        if(photosDTO == null) {
            return photos;
        }
        for (PhotoDTO photoDTO: photosDTO) {
            Photo photo = createPhoto(photoDTO);
            photo.setSize((long) photoDTO.getContent().getBytes().length);
            photo.setOwner(owner);
            photo.setOffer(offer);
            String newUuid = UUID.randomUUID().toString();
            photo.setUuid(newUuid);
            photoDTO.setUuid(newUuid);
            photos.add(photo);
        }
        return photos;
    }
}
