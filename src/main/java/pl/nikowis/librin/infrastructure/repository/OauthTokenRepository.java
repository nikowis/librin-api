package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.infrastructure.security.OauthAccessToken;

import java.util.List;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthAccessToken, String>, JpaSpecificationExecutor<Offer> {

    List<OauthAccessToken> findAllByUserName(String username);

}
