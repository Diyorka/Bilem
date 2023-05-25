package kg.bilem.service;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user);
    ResponseEntity<String> addAdmin(CreateUserDTO userDto);
}
