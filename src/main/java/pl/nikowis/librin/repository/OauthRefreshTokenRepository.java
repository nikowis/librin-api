package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.OauthRefreshToken;
import pl.nikowis.librin.model.Offer;

import java.util.List;

@Repository
public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, String>, JpaSpecificationExecutor<Offer> {

    void deleteAlLByTokenIdIn(List<String> tokenIds);

}
