package kg.bilem.service;

import kg.bilem.dto.user.ChangePasswordDTO;
import kg.bilem.dto.user.ResetPasswordDTO;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface PasswordService {
    ResponseEntity<String> forgotPassword(String userEmail);
    ResponseEntity<String> setNewPassword(String token, ResetPasswordDTO password);
    ResponseEntity<String> changePasswordOfUser(ChangePasswordDTO changePasswordDTO, User user);
}
