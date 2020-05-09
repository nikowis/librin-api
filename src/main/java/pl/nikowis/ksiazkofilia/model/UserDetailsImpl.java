package pl.nikowis.ksiazkofilia.model;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String role;
    private UserStatus status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.newArrayList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !UserStatus.DELETED.equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !UserStatus.BLOCKED.equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
