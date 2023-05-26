package kg.bilem.service.impls;

import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.User;
import kg.bilem.repository.CityRepository;
import kg.bilem.repository.RecoveryTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public Page<GetUserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toList());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public Page<GetUserDTO> getAllActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByStatus(Status.ACTIVE, pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toList());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user) {
        if (!userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistException("Пользователь с такой почтой уже зарегистрирован");
        }
        if(!cityRepository.existsById(userDto.getCityId())){
            throw new NotFoundException("Город с айди " + userDto.getCityId() + " не найден");
        }

        Long id = user.getId();
        modelMapper.getConfiguration().setSkipNullEnabled(false);
        modelMapper.map(userDto, user);
        user.setId(id);

        return toGetUserDto(userRepository.save(user));
    }

    @Override
    public ResponseEntity<String> addAdmin(CreateUserDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .status(Status.ACTIVE)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok("Администратор успешно добавлен");
    }
}
