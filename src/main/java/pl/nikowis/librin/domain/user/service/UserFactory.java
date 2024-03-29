package pl.nikowis.librin.domain.user.service;

import com.google.common.collect.Lists;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.policy.model.Policy;
import pl.nikowis.librin.domain.policy.model.PolicyType;
import pl.nikowis.librin.domain.token.model.Token;
import pl.nikowis.librin.domain.token.service.TokenFactory;
import pl.nikowis.librin.domain.user.dto.RegisterUserDTO;
import pl.nikowis.librin.domain.user.model.Consent;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;
import pl.nikowis.librin.infrastructure.repository.PolicyRepository;
import pl.nikowis.librin.infrastructure.security.SecurityConstants;
import pl.nikowis.librin.kernel.UserEmail;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFactory {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private TokenFactory tokenFactory;

    public User createNewUser(RegisterUserDTO userDTO) {
        User u = mapperFacade.map(userDTO, User.class);
        u.setEmail(new UserEmail(userDTO.getEmail()));
        u.setStatus(UserStatus.INACTIVE);
        u.setPassword(encoder.encode(userDTO.getPassword()));
        u.setRole(SecurityConstants.ROLE_USER);
        List<Consent> consents = createRegisterConsentList(u);
        u.setConsents(consents);
        Token confirmEmailToken = tokenFactory.createConfirmEmailToken(u);
        u.setTokens(Lists.newArrayList(confirmEmailToken));
        return u;
    }


    private List<Consent> createRegisterConsentList(User u) {
        List<Consent> consents = new ArrayList<>();
        for (PolicyType type : PolicyType.values()) {
            Policy policy = policyRepository.findFirstByTypeOrderByVersionDesc(type);
            Consent c = new Consent();
            c.setPolicy(policy);
            c.setUser(u);
            consents.add(c);
        }
        return consents;
    }

}
