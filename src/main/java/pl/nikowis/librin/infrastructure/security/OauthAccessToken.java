package pl.nikowis.librin.infrastructure.security;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class OauthAccessToken {

    @Id
    private String authenticationId;

    private String tokenId;

    private String clientId;

    private String userName;

    private String refreshToken;

}
