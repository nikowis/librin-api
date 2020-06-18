package pl.nikowis.librin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    Offer findByIdAndOwnerId(Long id, Long ownerId);

    List<Offer> findByStatusAndOwnerId(OfferStatus status, Long ownerId);

}
