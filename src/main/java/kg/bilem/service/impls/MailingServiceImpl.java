package kg.bilem.service.impls;

import kg.bilem.dto.mailing.RequestMailingDTO;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.model.Mailing;
import kg.bilem.repository.MailingRepository;
import kg.bilem.service.MailingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {
    private final MailingRepository mailingRepository;

    @Override
    public ResponseEntity<String> addToMailingList(RequestMailingDTO mailingDTO) {
        if(mailingRepository.existsByEmail(mailingDTO.getEmail())){
            throw new AlreadyExistException("Вы уже подписаны на рассылку");
        }

        mailingRepository.save(
                Mailing.builder()
                        .email(mailingDTO.getEmail())
                        .build()
        );
        return ResponseEntity.ok("Вы успешно подписались на рассылку");
    }
}
