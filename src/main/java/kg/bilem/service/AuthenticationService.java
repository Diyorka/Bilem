package kg.bilem.service;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.exception.UserAlreadyExistException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<String> register(CreateUserDTO request) throws UserAlreadyExistException;

    AuthenticationResponse authenticate(AuthUserDTO request);

    AuthenticationResponse refreshToken(String refreshToken);

    ResponseEntity<String> activateAccount(String token);
}
