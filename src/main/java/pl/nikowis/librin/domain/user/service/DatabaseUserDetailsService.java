package pl.nikowis.librin.domain.user.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserDetailsImpl;

@Service
@Transactional
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByEmail = userService.findUserByEmail(username.toLowerCase(LocaleContextHolder.getLocale()));
        if (userByEmail == null) {
            throw new UsernameNotFoundException("Username no found - " + username);
        }
        return mapperFacade.map(userByEmail, UserDetailsImpl.class);
    }
}
