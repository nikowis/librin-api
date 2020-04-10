package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.config.Profiles;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.model.UserDetailsImpl;
import pl.nikowis.ksiazkofilia.service.UserService;

@Service
@Transactional
@Profile("!" + Profiles.TEST)
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByLogin = userService.findUserByLogin(username);
        if (userByLogin == null) {
            throw new UsernameNotFoundException("Username no found - " + username);
        }
        return mapperFacade.map(userByLogin, UserDetailsImpl.class);
    }
}
