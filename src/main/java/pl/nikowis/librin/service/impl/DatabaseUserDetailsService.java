package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.model.UserDetailsImpl;
import pl.nikowis.librin.service.UserService;

@Service
@Transactional
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByEmail = userService.findUserByEmail(username);
        if (userByEmail == null) {
            throw new UsernameNotFoundException("Username no found - " + username);
        }
        return mapperFacade.map(userByEmail, UserDetailsImpl.class);
    }
}
