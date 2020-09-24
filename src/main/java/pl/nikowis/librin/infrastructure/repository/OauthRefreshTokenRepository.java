package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.infrastructure.security.OauthRefreshToken;

import java.util.List;

@Repository
public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, String>, JpaSpecificationExecutor<Offer> {

    void deleteAlLByTokenIdIn(List<String> tokenIds);

}
