package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.exception.PolicyNotFoundException;
import pl.nikowis.librin.model.Policy;
import pl.nikowis.librin.model.PolicyType;
import pl.nikowis.librin.repository.PolicyRepository;
import pl.nikowis.librin.service.PolicyService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyServiceImpl.class);

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private MapperFacade mapperFacade;

    private Path rootLocation;

    public PolicyServiceImpl(@Value("${policies.directory}") String policiesDirectory) {
        this.rootLocation = Paths.get(policiesDirectory);
    }

    @Override
    public File getPolicy(PolicyType type) {
        Policy policy = policyRepository.findFirstByTypeOrderByVersionDesc(type);
        if (policy == null) {
            LOGGER.error("Policy type {} not found in the database.", type.name());
            throw new PolicyNotFoundException();
        }
        Path resolved = rootLocation.resolve(policy.getFileName());
        File file = resolved.toFile();
        if (!file.exists()) {
            LOGGER.error("Policy file {} not found in the filesystem.", resolved.toString());
            throw new PolicyNotFoundException();
        }

        return file;
    }

}
