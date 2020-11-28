package pl.nikowis.librin.domain.report.service;

import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.conversation.model.Conversation;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.report.exception.IncorrectCreateReportRquestException;
import pl.nikowis.librin.domain.report.model.Report;
import pl.nikowis.librin.domain.report.model.ReportType;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.domain.user.model.UserStatus;

@Component
public class ReportFactory {

    private Report createReport(User currentUser, String description) {
        Report report = new Report();
        report.setReporter(currentUser);
        report.setDescription(description);
        return report;
    }

    Report createUserReport(User currentUser, String description, User reportedUser) {
        if (currentUser.getId().equals(reportedUser.getId()) || UserStatus.DELETED.equals(reportedUser.getStatus())) {
            throw new IncorrectCreateReportRquestException();
        }

        Report report = createReport(currentUser, description);
        report.setUser(reportedUser);
        report.setType(ReportType.USER);
        return report;
    }

    Report createConversationReport(User currentUser, String description, Conversation conv) {
        Report report = createReport(currentUser, description);
        report.setConversation(conv);
        report.setType(ReportType.CONVERSATION);
        return report;
    }

    Report createOfferReport(User currentUser, String description, Offer offer) {
        if (OfferStatus.DELETED.equals(offer.getStatus())) {
            throw new IncorrectCreateReportRquestException();
        }

        Report report = createReport(currentUser, description);
        report.setOffer(offer);
        report.setType(ReportType.OFFER);
        return report;
    }
}
