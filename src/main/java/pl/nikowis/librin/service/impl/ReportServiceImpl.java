package pl.nikowis.librin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.CreateReportDTO;
import pl.nikowis.librin.exception.IncorrectCreateReportRquestException;
import pl.nikowis.librin.model.Conversation;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.model.Report;
import pl.nikowis.librin.model.ReportType;
import pl.nikowis.librin.model.User;
import pl.nikowis.librin.model.UserStatus;
import pl.nikowis.librin.repository.ConversationRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.ReportRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.ReportService;
import pl.nikowis.librin.util.SecurityUtils;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public void createReport(CreateReportDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId).get();
        Report report = new Report();
        report.setReporter(currentUser);
        report.setDescription(dto.getDescription());
        if(dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(IncorrectCreateReportRquestException::new);
            if(dto.getUserId().equals(currentUserId) || UserStatus.DELETED.equals(user.getStatus())) {
                throw new IncorrectCreateReportRquestException();
            }
            report.setUser(user);
            report.setType(ReportType.USER);
        } else if(dto.getConversationId() != null) {
            Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(dto.getConversationId(), currentUserId).orElseThrow(IncorrectCreateReportRquestException::new);
            report.setConversation(conversation);
            report.setType(ReportType.CONVERSATION);
        } else if(dto.getOfferId() != null) {
            Offer offer = offerRepository.findById(dto.getOfferId()).orElseThrow(IncorrectCreateReportRquestException::new);
            if(OfferStatus.DELETED.equals(offer.getStatus())) {
                throw new IncorrectCreateReportRquestException();
            }
            report.setOffer(offer);
            report.setType(ReportType.OFFER);
        } else {
            throw new IncorrectCreateReportRquestException();
        }

        reportRepository.save(report);
    }
}
