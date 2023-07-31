package kg.bilem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.bilem.dto.mailing.RequestMailingDTO;
import kg.bilem.service.impls.MailingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mailing")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Контроллер для работы с рассылками",
        description = "В этом контроллере есть возможность добавления в список рассылок"
)
public class MailingController {
    private final MailingServiceImpl mailingService;

    @PostMapping("/add")
    @Operation(
            summary = "Подписаться на рассылку"
    )
    public ResponseEntity<String> addToMailingList(@RequestBody RequestMailingDTO mailingDTO){
        return mailingService.addToMailingList(mailingDTO);
    }
}
