package kg.bilem.service.impls;

import kg.bilem.dto.user.ChangePasswordDTO;
import kg.bilem.dto.user.ResetPasswordDTO;
import kg.bilem.exception.NotFoundException;
import kg.bilem.exception.TokenNotValidException;
import kg.bilem.model.User;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.PasswordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> forgotPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(
                        () -> new NotFoundException("Пользователь с почтой " + userEmail + " не найден")
                );

        String token = generateToken();
        user.setToken(token);
        userRepository.save(user);

        SimpleMailMessage activationEmail = new SimpleMailMessage();
        activationEmail.setFrom("bilem@gmail.com");
        activationEmail.setTo(user.getEmail());
        activationEmail.setSubject("Сброс пароля");
        activationEmail.setText("Для создания нового пароля введите следующий код: " + user.getToken());

        emailService.sendEmail(activationEmail);
        log.info("Код успешно отправлен на почту " + user.getEmail());

        return ResponseEntity.ok("Ваш код сброса пароля был отправлен на почту.");
    }

    @Override
    public ResponseEntity<String> setNewPassword(String token, ResetPasswordDTO password) {
        User user = userRepository.findByToken(token)
                .orElseThrow(
                        () -> new TokenNotValidException("Неверный токен.")
                );

        if(!password.getPassword().equals(password.getConfirmPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }

        user.setPassword(passwordEncoder.encode(password.getPassword()));
        user.setToken(null);
        userRepository.save(user);

        return ResponseEntity.ok("Пароль успешно сменен!");
    }

    @Override
    public ResponseEntity<String> changePasswordOfUser(ChangePasswordDTO changePasswordDTO, User user) {
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())){
            return ResponseEntity.badRequest().body("Старый пароль введен некорректно!");
        }
        if(passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())){
            return ResponseEntity.badRequest().body("Новый пароль совпадает со старым!");
        }
        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())){
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Пароль успешно сменен!");
    }

    private String generateToken(){
        Random random = new Random();
        String token = String.valueOf(random.nextInt(100000, 999999));
        while(userRepository.existsByToken(token)){
            token = String.valueOf(random.nextInt(100000, 999999));
        }

        return token;
    }
}
