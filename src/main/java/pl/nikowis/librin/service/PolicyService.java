package pl.nikowis.librin.service;

import pl.nikowis.librin.model.PolicyType;

import java.io.File;

public interface PolicyService {
    File getPolicy(PolicyType type);
}
