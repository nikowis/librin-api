package pl.nikowis.librin.domain.photo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.photo.model.Photo;
import pl.nikowis.librin.util.FilePathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${userfiles.directory}")
    private String userFilesDirectory;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Override
    public void storeOfferPhotos(Long ownerId, Long offerId, List<PhotoDTO> photos) {
        if (photos == null)
            return;
        photos.forEach(photo -> {
            Path resolvedFilePath = getFilePath(ownerId, offerId, photo.getOrder(), photo.getName());
            try {
                Files.createDirectories(resolvedFilePath.getParent());
                Files.write(resolvedFilePath, getFileBytes(photo.getContent()));
            } catch (IOException e) {
                LOGGER.error("Cant store file", e);
            }
        });
    }

    @Override
    public void removeOfferPhotos(Long ownerId, Long offerId, List<Photo> photos) {
        if (photos == null)
            return;

        photos.forEach(photo -> {
            Path resolvedFilePath = getFilePath(ownerId, offerId, photo.getOrder(), photo.getName());
            try {
                Files.delete(resolvedFilePath);
            } catch (IOException e) {
                LOGGER.error("Cant remove file", e);
            }
        });
    }

    private byte[] getFileBytes(String base64Content) {
        String imageDataBytes = base64Content.substring(base64Content.indexOf(",") + 1);
        return Base64.getDecoder().decode(imageDataBytes.getBytes());
    }

    private Path getFilePath(Long ownerId, Long offerId, Integer order, String name) {
        String filePath = FilePathUtils.getOfferPhotoPath(ownerId, offerId, order, name);
        return Paths.get(userFilesDirectory, filePath);
    }
}
