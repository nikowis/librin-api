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
        for (int i = 0; i < photosDTO.size(); i++) {
            PhotoDTO photoDTO = photosDTO.get(i);
            Photo photo = createPhoto(photoDTO);
            int order = i + 1;
            photo.setOrder(order);
            photoDTO.setOrder(order);
            photo.setSize((long) photoDTO.getContent().getBytes().length);
            photo.setOwner(owner);
            photo.setOffer(offer);
            photo.setOfferId(offer.getId());
            photos.add(photo);
        }
        return photos;
    }
}
