package kg.bilem.service;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<ResponseWithMessage> register(CreateUserDTO request) throws UserAlreadyExistException;

    AuthenticationResponse authenticate(AuthUserDTO request);

    AuthenticationResponse refreshToken(String refreshToken);

    ResponseEntity<ResponseWithMessage> activateAccount(String token);
    ResponseEntity<ResponseWithMessage> logout(User user);

    ResponseEntity<ResponseWithMessage> resendCode(String email);
}
