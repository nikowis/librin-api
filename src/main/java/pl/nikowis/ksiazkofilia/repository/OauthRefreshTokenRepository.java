package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.OauthRefreshToken;
import pl.nikowis.ksiazkofilia.model.OauthToken;
import pl.nikowis.ksiazkofilia.model.Offer;

import java.util.List;

@Repository
public interface OauthRefreshTokenRepository extends JpaRepository<OauthRefreshToken, String>, JpaSpecificationExecutor<Offer> {

    void deleteAlLByTokenIdIn(List<String> tokenIds);

}
