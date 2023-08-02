package kg.bilem.service.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.bilem.enums.CourseType;
import kg.bilem.enums.LessonType;
import kg.bilem.exception.FileEmptyException;
import kg.bilem.exception.NoAccessException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Lesson;
import kg.bilem.model.User;
import kg.bilem.repository.LessonRepository;
import kg.bilem.service.VideoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    LessonRepository lessonRepository;

    @Override
    public String saveVideo(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileEmptyException("Файл пустой");
        }

        final String urlKey = "cloudinary://298321212671499:SOxyj52dON_dPURTnhaTOCzswKY@bilem";

        File saveFile = Files.createTempFile(
                        System.currentTimeMillis() + "",
                        Objects.requireNonNull
                                        (file.getOriginalFilename(), "Файл должен иметь расширение")
                                .substring(file.getOriginalFilename().lastIndexOf("."))
                )
                .toFile();

        file.transferTo(saveFile);

        Cloudinary cloudinary = new Cloudinary((urlKey));

        Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.asMap("resource_type", "video"));

        return (String) upload.get("url");
    }

    @Override
    public ResponseEntity<String> saveVideoForLesson(Long lessonId, String videoUrl, MultipartFile video, User user) throws IOException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if(!user.getEmail().equals(lesson.getModule().getCourse().getOwner().getEmail())){
            throw new NoAccessException("У вас нет доступа к изменению данного курса");
        }

        if(lesson.getLessonType() != LessonType.VIDEO){
            throw new NoAccessException("Это не видеоурок");
        }

        if (lesson.getModule().getCourse().getCourseType() == CourseType.PAID) {
            lesson.setVideoUrl(saveVideo(video));
        } else if (videoUrl == null) {
            return ResponseEntity.badRequest().body("VideoUrl не должен быть пустым");
        } else {
            lesson.setVideoUrl(videoUrl);
        }

        lessonRepository.save(lesson);
        return ResponseEntity.ok("Видео успешно сохранено");
    }
}
