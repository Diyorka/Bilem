package kg.bilem.service.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.bilem.exception.FileEmptyException;
import kg.bilem.exception.NotFoundException;
import kg.bilem.model.Course;
import kg.bilem.model.User;
import kg.bilem.repository.CourseRepository;
import kg.bilem.repository.UserRepository;
import kg.bilem.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public String saveImage(MultipartFile file) throws IOException {
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

        Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.emptyMap());

        return (String) upload.get("url");
    }

    @Override
    public ResponseEntity<String> saveForUser(User user, MultipartFile file) throws IOException {
        user.setImageUrl(saveImage(file));
        userRepository.save(user);
        return ResponseEntity.ok("Фотография сохранена");
    }

    @Override
    public ResponseEntity<String> saveForCourse(Long courseId, MultipartFile file, User user) throws IOException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс с айди " + courseId + " не найден"));

        course.setImageUrl(saveImage(file));
        courseRepository.save(course);

        return ResponseEntity.ok("Фотография курса успешно добавлена");
    }
}
