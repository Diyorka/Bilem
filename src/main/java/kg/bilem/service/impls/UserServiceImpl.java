package kg.bilem.service.impls;

import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.User;
import kg.bilem.repository.RecoveryTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecoveryTokenRepository recoveryTokenRepository;
    private final ModelMapper modelMapper;

    @Override
    public GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user) {
        if (!userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistException("Пользователь с такой почтой уже зарегистрирован");
        }

        modelMapper.map(userDto, user);
        userRepository.save(user);

        return toGetUserDto(user);
    }

    @Override
    public ResponseEntity<String> addAdmin(CreateUserDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );

        var user = buildUser(userDto);
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        return ResponseEntity.ok("Администратор успешно добавлен");
    }

    private User buildUser(CreateUserDTO userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.STUDENT)
                .status(Status.NOT_ACTIVATED)
                .build();
    }

}
