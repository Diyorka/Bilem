package kg.bilem.service.impls;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.dto.user.ChangePasswordDTO;
import kg.bilem.dto.user.ResetPasswordDTO;
import kg.bilem.exception.NotFoundException;
import kg.bilem.exception.TokenNotValidException;
import kg.bilem.model.RecoveryToken;
import kg.bilem.model.User;
import kg.bilem.repository.RecoveryTokenRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final RecoveryTokenRepository recoveryTokenRepository;
    @Value(value = "${app.recoveryValidityInSeconds}")
    private long recoveryValidityInSeconds;

    @Override
    public ResponseEntity<ResponseWithMessage> forgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(
                        () -> new NotFoundException("Пользователь с почтой " + userEmail + " не найден")
                );

        if (!recoveryTokenRepository.existsByUser(user)) {
            RecoveryToken recoveryToken = constructToken(user);
            recoveryTokenRepository.save(recoveryToken);
            sendToken(recoveryToken, user);
        } else {
            RecoveryToken getToken = recoveryTokenRepository.findByUser(user);
            if(Duration.between(getToken.getCreatedAt(), LocalDateTime.now()).toSeconds() < 59){
                return ResponseEntity.badRequest().body(new ResponseWithMessage("С последнего запроса прошло меньше минуты, попробуйте повторно позже"));
            }
            recoveryTokenRepository.delete(getToken);
            RecoveryToken recoveryToken = constructToken(user);
            recoveryTokenRepository.save(recoveryToken);
            sendToken(recoveryToken, user);
        }

        return ResponseEntity.ok(new ResponseWithMessage("Ваш код сброса пароля был отправлен на почту."));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> setNewPassword(String token, ResetPasswordDTO password) {
        RecoveryToken recoveryToken = recoveryTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new TokenNotValidException("Неверный код")
                );

        if (LocalDateTime.now().isAfter(recoveryToken.getExpireAt())) {
            throw new TokenNotValidException("Время действия кода истекло, запросите новый");
        }

        User user = userRepository.findById(recoveryToken.getUser().getId())
                .orElseThrow(
                        () -> new TokenNotValidException("Пользователь не найден")
                );

        if (!password.getPassword().equals(password.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Пароли не совпадают!"));
        }

        user.setPassword(passwordEncoder.encode(password.getPassword()));
        userRepository.save(user);

        recoveryToken.setToken(null);
        recoveryToken.setExpireAt(null);
        recoveryToken.setCreatedAt(null);
        recoveryTokenRepository.save(recoveryToken);

        return ResponseEntity.ok(new ResponseWithMessage("Пароль успешно сменен!"));
    }

    @Override
    public ResponseEntity<ResponseWithMessage> changePasswordOfUser(ChangePasswordDTO changePasswordDTO, User user) {
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Старый пароль введен некорректно!"));
        }
        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Новый пароль совпадает со старым!"));
        }
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().body(new ResponseWithMessage("Пароли не совпадают!"));
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseWithMessage("Пароль успешно сменен!"));
    }

    private void sendToken(RecoveryToken recoveryToken, User user) {
        SimpleMailMessage activationEmail = new SimpleMailMessage();
        activationEmail.setFrom("bilem@gmail.com");
        activationEmail.setTo(user.getEmail());
        activationEmail.setSubject("Сброс пароля");
        activationEmail.setText("Для создания нового пароля введите следующий код: " + recoveryToken.getToken() +
                "\nИстекает " + recoveryToken.getExpireAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        emailService.sendEmail(activationEmail);
        log.info("Код успешно отправлен на почту " + user.getEmail());
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
                .expireAt(LocalDateTime.now().plusSeconds(recoveryValidityInSeconds))
                .createdAt(LocalDateTime.now())
                .build();
    }
}
