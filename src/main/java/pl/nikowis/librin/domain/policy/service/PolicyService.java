package pl.nikowis.librin.domain.policy.service;

import pl.nikowis.librin.domain.policy.model.PolicyType;

import java.io.File;

public interface PolicyService {
    File getPolicy(PolicyType type);
}
