package kg.bilem.service.impls;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.TokenNotValidException;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.RefreshToken;
import kg.bilem.model.User;
import kg.bilem.repository.RefreshTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<String> register(CreateUserDTO request) throws UserAlreadyExistException {
        if (repository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );
        if (repository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistException(
                    "username",
                    "Пользователь с таким никнеймом уже существует"
            );
        if(!request.getPassword().equals(request.getConfirmPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }

        Random random = new Random();
        String token = String.valueOf(random.nextInt(100000, 999999));
        while (repository.existsByToken(token)) {
            token = String.valueOf(random.nextInt(100000, 999999));
        }

        var user = buildUser(request);
        user.setToken(token);
        repository.save(user);

        SimpleMailMessage activationEmail = new SimpleMailMessage();
        activationEmail.setFrom("laptopKG@gmail.com");
        activationEmail.setTo(user.getEmail());
        activationEmail.setSubject("Активация аккаунта");
        activationEmail.setText("Для активации аккаунта введите следующий код: " + user.getToken());

        emailService.sendEmail(activationEmail);
        log.info("Код успешно отправлен на почту " + user.getEmail());

        return ResponseEntity.ok("Успешная регистрация! Ваш код активации был отправлен на почту.");
    }

    public AuthenticationResponse authenticate(AuthUserDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        if (refreshTokenRepository.existsByUserId(user.getId())) {
            RefreshToken refToken = refreshTokenRepository.findByUserId(user.getId());
            refToken.setToken(refreshToken);
            refreshTokenRepository.save(refToken);
        } else {
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .token(refreshToken)
                            .userId(user.getId())
                            .build()
            );
        }

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        if (!refreshTokenRepository.existsByToken(refreshToken)) {
            throw new TokenNotValidException("Токен не валидный");
        }

        final String userEmail;
        userEmail = jwtService.extractUsername(refreshToken);
        var user = repository.findByEmail(userEmail).orElseThrow();

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new TokenNotValidException("Токен не валидный");
        }

        RefreshToken refToken = refreshTokenRepository.findByToken(refreshToken);
        var newRefreshToken = jwtService.generateRefreshToken(user);
        refToken.setToken(newRefreshToken);
        refreshTokenRepository.save(refToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(newRefreshToken)
                .build();
    }

    public ResponseEntity<String> activateAccount(String token) {
        Optional<User> user = repository.findByToken(token);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный код");
        }

        User activateUser = user.get();
        activateUser.setStatus(Status.ACTIVE);

        activateUser.setToken(null);
        repository.save(activateUser);
        return ResponseEntity.ok().body("Аккаунт успешно активирован!");

    }

    @Override
    public GetUserDTO changeUserInfo(UpdateUserDTO userDto, User user) {
        if (!userDto.getEmail().equals(user.getEmail()) && repository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistException("Пользователь с такой почтой уже зарегистрирован");
        }

        modelMapper.map(userDto, user);
        repository.save(user);

        return toGetUserDto(user);
    }

    @Override
    public ResponseEntity<String> addAdmin(CreateUserDTO userDto) {
        if (repository.existsByEmail(userDto.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );
        if (repository.existsByUsername(userDto.getUsername()))
            throw new UserAlreadyExistException(
                    "username",
                    "User with this username is already exists"
            );

        var user = buildUser(userDto);
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
        repository.save(user);

        return ResponseEntity.ok("Администратор успешно добавлен");
    }

    private User buildUser(CreateUserDTO userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.STUDENT)
                .status(Status.NOT_ACTIVATED)
                .token(null)
                .build();
    }
}
