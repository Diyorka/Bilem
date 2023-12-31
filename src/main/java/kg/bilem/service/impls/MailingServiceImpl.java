package kg.bilem.service.impls;

import kg.bilem.dto.mailing.RequestMailingDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.exception.AlreadyExistException;
import kg.bilem.model.Mailing;
import kg.bilem.repository.MailingRepository;
import kg.bilem.service.MailingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MailingServiceImpl implements MailingService {
    MailingRepository mailingRepository;

    @Override
    public ResponseEntity<ResponseWithMessage> addToMailingList(RequestMailingDTO mailingDTO) {
        if(mailingRepository.existsByEmail(mailingDTO.getEmail())){
            throw new AlreadyExistException("Вы уже подписаны на рассылку");
        }

        mailingRepository.save(
                Mailing.builder()
                        .email(mailingDTO.getEmail())
                        .build()
        );
        return ResponseEntity.ok(new ResponseWithMessage("Вы успешно подписались на рассылку"));
    }
}
