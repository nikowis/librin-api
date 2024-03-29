package pl.nikowis.librin.domain.city.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.city.model.City;
import pl.nikowis.librin.infrastructure.repository.CityRepository;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> findCities(String query) {
        return cityRepository.findCities(query);
    }
}
