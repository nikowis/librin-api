package pl.nikowis.librin.domain.token.service;

import pl.nikowis.librin.domain.token.dto.ChangeUserPasswordDTO;
import pl.nikowis.librin.domain.token.dto.GenerateAccountActivationEmailDTO;
import pl.nikowis.librin.domain.token.dto.GenerateResetPasswordDTO;

import java.util.UUID;

public interface TokenService {

    void confirmUserEmail(UUID tokenId);

    void changeUserPassword(UUID tokenId, ChangeUserPasswordDTO userDTO);

    void generateResetPasswordToken(GenerateResetPasswordDTO dto);

    void generateAccountActivationToken(GenerateAccountActivationEmailDTO dto);

}
