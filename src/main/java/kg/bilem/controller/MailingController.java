package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.mailing.RequestMailingDTO;
import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.service.impls.MailingServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mailing")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с рассылками",
        description = "В этом контроллере есть возможность добавления в список рассылок"
)
public class MailingController {
    MailingServiceImpl mailingService;

    @PostMapping("/add")
    @Operation(
            summary = "Подписаться на рассылку"
    )
    public ResponseEntity<ResponseWithMessage> addToMailingList(@RequestBody RequestMailingDTO mailingDTO){
        return mailingService.addToMailingList(mailingDTO);
    }
}
