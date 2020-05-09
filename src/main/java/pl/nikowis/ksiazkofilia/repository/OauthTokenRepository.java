package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.OauthAccessToken;
import pl.nikowis.ksiazkofilia.model.Offer;

import java.util.List;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthAccessToken, String>, JpaSpecificationExecutor<Offer> {

    List<OauthAccessToken> findAllByUserName(String username);

}
