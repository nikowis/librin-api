package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;
import pl.nikowis.ksiazkofilia.exception.CantStoreEmptyFileException;
import pl.nikowis.ksiazkofilia.repository.AttachmentRepository;
import pl.nikowis.ksiazkofilia.service.AttachmentService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Value("${attachments.directory}")
    private String attachmentDirectory;

    private Path rootLocation;

    public AttachmentServiceImpl() {
//        this.rootLocation = Paths.get(attachmentDirectory);
    }

    @Override
    public AttachmentDTO getAttachment(Long attachmentId) {
//        return rootLocation.resolve(filename);
        return null;
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        attachmentRepository.
        try {
            if (file.isEmpty()) {
                throw new CantStoreEmptyFileException();
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
//            throw new StorageException("Failed to store attachment " + filename, e);
        }
    }
}
