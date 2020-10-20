package pl.nikowis.librin.domain.photo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.util.FilePathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${userfiles.directory}")
    private String userFilesDirectory;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);



    @Override
    public void storeFile(String filePath, String base64Content) {
        if (filePath == null)
            return;
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            Files.createDirectories(resolvedFilePath.getParent());
            Files.write(resolvedFilePath, getFileBytes(base64Content));
        } catch (IOException e) {
            LOGGER.error("Cant store file", e);
        }
    }

    @Override
    public void storeFiles(Map<String, String> pathToContentMap) {
        if (pathToContentMap == null)
            return;
        pathToContentMap.forEach(this::storeFile);
    }

    @Override
    public void removeFile(String filePath) {
        if (filePath == null)
            return;
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            Files.delete(resolvedFilePath);
        } catch (IOException e) {
            LOGGER.error("Cant remove file", e);
        }
    }

    @Override
    public void removeFiles(List<String> filePaths) {
        if (filePaths == null)
            return;
        filePaths.forEach(this::removeFile);
    }

    private byte[] getFileBytes(String base64Content) {
        String imageDataBytes = base64Content.substring(base64Content.indexOf(",") + 1);
        return Base64.getDecoder().decode(imageDataBytes.getBytes());
    }

}
