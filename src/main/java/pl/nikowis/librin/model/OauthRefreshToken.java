package pl.nikowis.librin.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class OauthRefreshToken {
    @Id
    private String tokenId;

}
