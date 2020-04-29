package pl.nikowis.ksiazkofilia.service;

import org.springframework.web.multipart.MultipartFile;
import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;

import java.util.List;

public interface AttachmentService {

    AttachmentDTO getAttachment(Long attachmentId);

//    List<AttachmentDTO> getAttachment(List<Long> attachmentId);

    void store(MultipartFile file);
}
