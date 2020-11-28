package pl.nikowis.librin.domain.report.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.conversation.model.Conversation;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.report.dto.CreateReportDTO;
import pl.nikowis.librin.domain.report.exception.IncorrectCreateReportRquestException;
import pl.nikowis.librin.domain.report.model.Report;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.ConversationRepository;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.ReportRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
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

    @Autowired
    private ReportFactory reportFactory;

    @Override
    public void createReport(CreateReportDTO dto) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId).get();

        Report report = null;

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElseThrow(IncorrectCreateReportRquestException::new);
            report = reportFactory.createUserReport(currentUser, dto.getDescription(), user);
        } else if (dto.getConversationId() != null) {
            Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(dto.getConversationId(), currentUserId)
                    .orElseThrow(IncorrectCreateReportRquestException::new);
            report = reportFactory.createConversationReport(currentUser, dto.getDescription(), conversation);
        } else if (dto.getOfferId() != null) {
            Offer offer = offerRepository.findById(dto.getOfferId()).orElseThrow(IncorrectCreateReportRquestException::new);
            report = reportFactory.createOfferReport(currentUser, dto.getDescription(), offer);
        } else {
            throw new IncorrectCreateReportRquestException();
        }

        reportRepository.save(report);
    }
}
