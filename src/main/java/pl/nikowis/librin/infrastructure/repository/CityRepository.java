package pl.nikowis.librin.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nikowis.librin.domain.city.model.City;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    //    https://stackoverflow.com/questions/58621139/spring-jpa-define-sql-function-for-usage-in-custom-query
    @Query(nativeQuery = true, value = "SELECT * FROM city WHERE lower(public.f_unaccent(name)) LIKE lower(public.f_unaccent(CONCAT(?1,'%'))) ORDER BY type ASC LIMIT 10")
    List<City> findCities(@Param("query") String query);
}
