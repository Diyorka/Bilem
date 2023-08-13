package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile file) throws IOException;
    ResponseEntity<ResponseWithMessage> saveForUser(User user, MultipartFile file) throws IOException;

    ResponseEntity<ResponseWithMessage> saveForCourse(Long courseId, MultipartFile file, User user) throws IOException;
}
