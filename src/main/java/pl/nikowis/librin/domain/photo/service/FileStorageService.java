package pl.nikowis.librin.domain.photo.service;

import java.util.List;
import java.util.Map;

public interface FileStorageService {
    void storeFile(String filePath, String base64Content);

    void storeFiles(Map<String, String> pathToContentMap);

    void removeFile(String filePath);

    void removeFiles(List<String> filePaths);
}
