package kg.bilem.service;

import kg.bilem.dto.mailing.RequestMailingDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import org.springframework.http.ResponseEntity;

public interface MailingService {
    ResponseEntity<ResponseWithMessage> addToMailingList(RequestMailingDTO mailingDTO);
}
