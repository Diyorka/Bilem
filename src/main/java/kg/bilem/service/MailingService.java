package kg.bilem.service;

import kg.bilem.dto.mailing.RequestMailingDTO;
import org.springframework.http.ResponseEntity;

public interface MailingService {
    ResponseEntity<String> addToMailingList(RequestMailingDTO mailingDTO);
}
