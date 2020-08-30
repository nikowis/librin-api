package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.model.City;
import pl.nikowis.librin.service.CityService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = CityController.CITIES_ENDPOINT)
public class CityController {

    public static final String CITIES_ENDPOINT = "/cities";

    @Autowired
    private CityService cityService;

    @GetMapping
    public List<City> findCities(@NotNull @RequestParam String query) {
        return cityService.findCities(query);
    }

}
