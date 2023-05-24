package kg.bilem.service.impls;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.dto.user.GetUserDTO;
import kg.bilem.dto.user.UpdateUserDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.exception.TokenNotValidException;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.RecoveryToken;
import kg.bilem.model.RefreshToken;
import kg.bilem.model.User;
import kg.bilem.repository.RecoveryTokenRepository;
import kg.bilem.repository.RefreshTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static kg.bilem.dto.user.GetUserDTO.toGetUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RecoveryTokenRepository recoveryTokenRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<String> register(CreateUserDTO request) throws UserAlreadyExistException {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistException(
                    "email",
                    "Пользователь с такой почтой уже существует"
            );

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }

        var user = buildUser(request);
        userRepository.save(user);

        RecoveryToken recoveryToken = constructToken(user);
        recoveryTokenRepository.save(recoveryToken);

        SimpleMailMessage activationEmail = new SimpleMailMessage();
        activationEmail.setFrom("bilem@gmail.com");
        activationEmail.setTo(user.getEmail());
        activationEmail.setSubject("Активация аккаунта");
        activationEmail.setText("Для активации аккаунта введите следующий код: " + recoveryToken.getToken() +
                                "\nИстекает " + recoveryToken.getExpireAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

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
        var user = userRepository.findByEmail(request.getEmail())
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
        var user = userRepository.findByEmail(userEmail).orElseThrow();

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
        RecoveryToken recoveryToken = recoveryTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new TokenNotValidException("Неверный код")
                );

        User activateUser = userRepository.findById(recoveryToken.getUser().getId())
                .orElseThrow(
                        () -> new NotFoundException("Пользователь не найден")
                );

        activateUser.setStatus(Status.ACTIVE);
        userRepository.save(activateUser);

        recoveryToken.setToken(null);
        recoveryToken.setExpireAt(null);
        recoveryToken.setCreatedAt(null);
        recoveryTokenRepository.save(recoveryToken);

        return ResponseEntity.ok("Аккаунт успешно активирован!");
    }

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

    private RecoveryToken constructToken(User user) {
        Random random = new Random();
        String token = String.valueOf(random.nextInt(100000, 999999));
        while (recoveryTokenRepository.existsByToken(token)) {
            token = String.valueOf(random.nextInt(100000, 999999));
        }

        return RecoveryToken.builder()
                .user(user)
                .token(token)
                .expireAt(LocalDateTime.now().plusMonths(6))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
