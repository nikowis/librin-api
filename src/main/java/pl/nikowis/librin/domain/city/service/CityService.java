package pl.nikowis.librin.domain.city.service;

import pl.nikowis.librin.domain.city.model.City;

import java.util.List;

public interface CityService {
    List<City> findCities(String query);
}
