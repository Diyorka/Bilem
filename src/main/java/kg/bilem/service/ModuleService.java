package kg.bilem.service;

import kg.bilem.dto.module.RequestModuleDTO;
import kg.bilem.dto.module.ResponseModuleDTO;
import kg.bilem.model.User;

public interface ModuleService {
    ResponseModuleDTO createModule(RequestModuleDTO moduleDTO, User user);
}
