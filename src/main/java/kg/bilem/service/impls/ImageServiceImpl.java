package kg.bilem.service.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.bilem.exception.FileEmptyException;
import kg.bilem.model.User;
import kg.bilem.repository.UserRepository;
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
public class ImageServiceImpl {
    private final UserRepository userRepository;

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileEmptyException("Файл пустой");
        }

        final String urlKey = "cloudinary://753949556892917:SCszCjA1duCgeAaMxDP-7Qq3dP8@dja0nqat2";

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

    public ResponseEntity<String> saveForUser(User user, MultipartFile file) throws IOException {
        user.setImage_url(saveImage(file));
        userRepository.save(user);
        return ResponseEntity.ok("Фотография сохранена");
    }
}
