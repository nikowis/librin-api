package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;
import pl.nikowis.ksiazkofilia.service.AttachmentService;


@RestController
@RequestMapping(path = AttachmentController.ATTRACHMENTS_ENDPOINT)
public class AttachmentController {

    public static final String ATTRACHMENTS_ENDPOINT = "/attachment";
    public static final String ATTRACHMENT_ID_VARIABLE = "attachmentId";
    public static final String ATTRACHMENT_PATH = "/{" + ATTRACHMENT_ID_VARIABLE + "}";
    public static final String ATTRACHMENT_ENDPOINT = ATTRACHMENTS_ENDPOINT + ATTRACHMENT_PATH;

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping(path = ATTRACHMENT_PATH)
    public AttachmentDTO getAttachment(@PathVariable(ATTRACHMENT_ID_VARIABLE) Long attachmentId) {
        return attachmentService.getAttachment(attachmentId);
    }

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("attachment") MultipartFile file) {
        attachmentService.store(file);
    }

}
