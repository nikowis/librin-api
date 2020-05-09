package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.OauthToken;
import pl.nikowis.ksiazkofilia.model.Offer;

import java.util.List;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, String>, JpaSpecificationExecutor<Offer> {

    List<OauthToken> findAllByUserName(String username);

}
