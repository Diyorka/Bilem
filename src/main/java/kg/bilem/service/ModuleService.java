package kg.bilem.service;

import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ModuleService {
    ResponseModuleDTO createModule(RequestModuleDTO moduleDTO, User user);

    ResponseEntity<String> editModule(Long moduleId, RequestModuleDTO moduleDTO, User user);

    ResponseEntity<String> deleteModule(Long moduleId, User user);

    Page<ResponseModuleDTO> getModulesByCourseId(Long courseId, Pageable pageable, User user);
}
