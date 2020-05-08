package pl.nikowis.ksiazkofilia.service;

import pl.nikowis.ksiazkofilia.model.PolicyType;

import java.io.File;

public interface PolicyService {
    File getPolicy(PolicyType type);
}
