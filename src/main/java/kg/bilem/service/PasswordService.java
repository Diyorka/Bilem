package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.user.ChangePasswordDTO;
import kg.bilem.dto.user.ResetPasswordDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface PasswordService {
    ResponseEntity<ResponseWithMessage> forgotPassword(String userEmail);
    ResponseEntity<ResponseWithMessage> setNewPassword(String token, ResetPasswordDTO password);
    ResponseEntity<ResponseWithMessage> changePasswordOfUser(ChangePasswordDTO changePasswordDTO, User user);
}
