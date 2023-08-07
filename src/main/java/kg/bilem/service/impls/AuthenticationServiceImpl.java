package kg.bilem.service.impls;

import kg.bilem.dto.AuthenticationResponse;
import kg.bilem.dto.user.AuthUserDTO;
import kg.bilem.dto.user.CreateUserDTO;
import kg.bilem.enums.Role;
import kg.bilem.enums.Status;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.exception.TokenNotValidException;
import kg.bilem.exception.UserAlreadyExistException;
import kg.bilem.model.RecoveryToken;
import kg.bilem.model.RefreshToken;
import kg.bilem.model.User;
import kg.bilem.repository.RecoveryTokenRepository;
import kg.bilem.repository.RefreshTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RecoveryTokenRepository recoveryTokenRepository;

    @Value(value = "${app.refreshExpirationInMs}")
    private long refreshExpirationInMs;

    @Override
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
        log.info("your code: {}", recoveryToken.getToken());

        sendToken(recoveryToken, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public AuthenticationResponse authenticate(AuthUserDTO request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с такой почтой не найден"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new NoAccessException("Вы ввели неверный пароль");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = generateRefreshToken(user);
        if (refreshTokenRepository.existsByUser(user)) {
            refreshToken.setId(refreshTokenRepository.findByUser(user).getId());
        }
        refreshTokenRepository.save(refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByToken(refreshToken)
                .map(this::verifyExpiration)
                .orElseThrow(
                        () -> new TokenNotValidException("Токен не валидный")
                );

        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(refToken.getUser()))
                .refreshToken(refToken.getToken())
                .build();
    }

    @Override
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

        recoveryTokenRepository.delete(recoveryToken);

        return ResponseEntity.ok("Аккаунт успешно активирован!");
    }

    @Override
    public ResponseEntity<String> logout(User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
        refreshTokenRepository.delete(refreshToken);
        return ResponseEntity.ok("Успешный выход");
    }

    @Override
    public ResponseEntity<String> resendCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("Пользователь с такой почтой не найден");
        }

        User user = userRepository.findByEmail(email).get();
        RecoveryToken recoveryToken = recoveryTokenRepository.findByUser(user);
        if (Duration.between(recoveryToken.getCreatedAt(), LocalDateTime.now()).toSeconds() < 59) {
            return ResponseEntity.badRequest().body("С последнего запроса прошло меньше минуты, попробуйте повторно позже");
        }

        recoveryTokenRepository.delete(recoveryToken);
        RecoveryToken newToken = constructToken(user);
        recoveryTokenRepository.save(newToken);
        sendToken(newToken, user);

        return ResponseEntity.ok("Код успешно отправлен");
    }

    private void sendToken(RecoveryToken recoveryToken, User user) {
        SimpleMailMessage activationEmail = new SimpleMailMessage();
        activationEmail.setFrom("bilem@gmail.com");
        activationEmail.setTo(user.getEmail());
        activationEmail.setSubject("Активация аккаунта");
        activationEmail.setText("Для активации аккаунта введите следующий код: " + recoveryToken.getToken() +
                "\nИстекает " + recoveryToken.getExpireAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        emailService.sendEmail(activationEmail);
        log.info("Код успешно отправлен на почту " + user.getEmail());
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
                .expireAt(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationInMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenNotValidException("Время токена истекло. Пожалуйста, авторизуйтесь повторно.");
        }

        return token;
    }
}
