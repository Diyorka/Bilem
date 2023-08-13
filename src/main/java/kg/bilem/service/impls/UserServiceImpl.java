package kg.bilem.service.impls;

import kg.bilem.dto.other.ResponseWithMessage;
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
import kg.bilem.repository.UserRepository;
import kg.bilem.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    CityRepository cityRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public List<GetUserDTO> getTeachersByName(String name) {
        List<User> users = userRepository.findByNameContainsIgnoreCaseAndStatusAndRole(name, Status.ACTIVE, Role.TEACHER);
        return toGetUserDto(new HashSet<>(users));
    }

    @Override
    public Page<GetUserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toSet());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public Page<GetUserDTO> getAllActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByStatus(Status.ACTIVE, pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toSet());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public Page<GetUserDTO> getAllStudents(Pageable pageable) {
        Page<User> users = userRepository.findAllByStatusAndRole(Status.ACTIVE, Role.STUDENT, pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toSet());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public Page<GetUserDTO> getAllTeachers(Pageable pageable) {
        Page<User> users = userRepository.findAllByStatusAndRole(Status.ACTIVE, Role.TEACHER, pageable);
        List<GetUserDTO> userDTOS = toGetUserDto(users.toSet());
        return new PageImpl<>(userDTOS, pageable, users.getTotalElements());
    }

    @Override
    public GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user) {
        if (!userDto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistException("Пользователь с такой почтой уже зарегистрирован");
        }

        buildUser(user, userDto);
        return toGetUserDto(userRepository.save(user));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> addAdmin(CreateUserDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Пароли не совпадают"));
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .status(Status.ACTIVE)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseWithMessage("Администратор успешно добавлен"));
    }

    @Override
    public GetUserDTO getUserInfo(User user) {
        return toGetUserDto(user);
    }

    private void buildUser(User user, UpdateUserDTO userDto) {
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout_me(userDto.getAbout_me());
        user.setActivity_sphere(userDto.getActivity_sphere());
        user.setProfile_description(userDto.getProfile_description());
        user.setCity(cityRepository.findById(userDto.getCityId()).orElseThrow(() -> new NotFoundException("Город с айди " + userDto.getCityId() + " не найден")));
        user.setWorkPlace(userDto.getWork_place());
        user.setInstagram(userDto.getInstagram());
        user.setGithub(userDto.getGithub());
        user.setBehance(userDto.getBehance());
        user.setTwitter(userDto.getTwitter());
        user.setYoutube(userDto.getYoutube());
        user.setTelegram(userDto.getTelegram());
        user.setDribble(userDto.getDribble());
    }
}
