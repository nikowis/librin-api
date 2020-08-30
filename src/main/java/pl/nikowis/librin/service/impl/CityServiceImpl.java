package pl.nikowis.librin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.exception.IncorrectCreateReportRquestException;
import pl.nikowis.librin.model.City;
import pl.nikowis.librin.model.Conversation;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.Report;
import pl.nikowis.librin.model.ReportType;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.model.UserStatus;
import pl.nikowis.librin.repository.CityRepository;
import pl.nikowis.librin.repository.ConversationRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.ReportRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.CityService;
import pl.nikowis.librin.service.ReportService;
import pl.nikowis.librin.util.SecurityUtils;

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
