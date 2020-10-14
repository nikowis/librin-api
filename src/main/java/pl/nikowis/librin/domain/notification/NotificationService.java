package pl.nikowis.librin.domain.notification;

import pl.nikowis.librin.kernel.UserEmail;

import java.util.UUID;

public interface NotificationService {

    void notifyUserRegistered(UserEmail email, UUID tokenId, String confirmEmailBaseUrl);

    void sendUserResetPasswordRequestCreated(UserEmail email, UUID id, String confirmEmailBaseUrl);
}
