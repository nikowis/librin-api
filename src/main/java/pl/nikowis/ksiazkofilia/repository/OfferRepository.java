package pl.nikowis.ksiazkofilia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.nikowis.ksiazkofilia.model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    Page<Offer> findByOwnerId(Long id, Pageable pageable);

    Offer findByIdAndOwnerId(Long id, Long ownerId);

    Page<Offer> findByActiveAndOwnerId(Boolean active, Long ownerId, Pageable pageable);

}
