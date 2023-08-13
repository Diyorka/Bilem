package kg.bilem.service;

import kg.bilem.dto.other.ResponseWithMessage;
import kg.bilem.model.Lesson;
import kg.bilem.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    String saveVideo(MultipartFile file, Lesson lesson) throws IOException;

    ResponseEntity<ResponseWithMessage> saveVideoForLesson(Long lessonId, String videoUrl, MultipartFile file, User user) throws IOException;
}
