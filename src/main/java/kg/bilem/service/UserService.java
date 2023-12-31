package kg.bilem.service;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<GetUserDTO> getTeachersByName(String name);
    Page<GetUserDTO> getAllUsers(Pageable pageable);

    Page<GetUserDTO> getAllActiveUsers(Pageable pageable);

    Page<GetUserDTO> getAllStudents(Pageable pageable);

    Page<GetUserDTO> getAllTeachers(Pageable pageable);

    GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user);

    ResponseEntity<ResponseWithMessage> addAdmin(CreateUserDTO userDto);

    GetUserDTO getUserInfo(User user);

}
