package kg.bilem.service;

import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile file) throws IOException;
    ResponseEntity<String> saveForUser(User user, MultipartFile file) throws IOException;
}
