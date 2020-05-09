package pl.nikowis.ksiazkofilia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.ksiazkofilia.model.PolicyType;
import pl.nikowis.ksiazkofilia.service.PolicyService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
public class PolicyController {

    public static final String POLICIES_ENDPOINT = "/policies";
    public static final String POLICY_TYPE_VARIABLE = "policyType";
    public static final String POLICY_PATH = "/{" + POLICY_TYPE_VARIABLE + "}";
    public static final String POLICY_ENDPOINT = POLICIES_ENDPOINT + POLICY_PATH;

    @Autowired
    private PolicyService policyService;

    @GetMapping(value = POLICY_ENDPOINT)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getPolicy(@PathVariable(POLICY_TYPE_VARIABLE) PolicyType type, HttpServletResponse response) throws FileNotFoundException {
        File policyFile = policyService.getPolicy(type);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(policyFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + policyFile.getName())
                .contentType(MediaType.APPLICATION_PDF).contentLength(policyFile.length())
                .body(resource);
    }


}
