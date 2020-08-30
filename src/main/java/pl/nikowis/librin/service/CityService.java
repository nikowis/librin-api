package pl.nikowis.librin.service;

import pl.nikowis.librin.model.City;

import java.util.List;

public interface CityService {
    List<City> findCities(String query);
}
