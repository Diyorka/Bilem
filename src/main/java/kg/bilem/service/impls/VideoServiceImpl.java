package kg.bilem.service.impls;

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
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    LessonRepository lessonRepository;
    OkHttpClient client;
    String key = "Bearer 3bd30cb6-b312-47c3-8a07-5f4fb1c4f4b9";

//    @Override
//    public String saveVideo(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            throw new FileEmptyException("Файл пустой");
//        }
//
//        final String urlKey = "cloudinary://298321212671499:SOxyj52dON_dPURTnhaTOCzswKY@bilem";
//
//        File saveFile = Files.createTempFile(
//                        System.currentTimeMillis() + "",
//                        Objects.requireNonNull
//                                        (file.getOriginalFilename(), "Файл должен иметь расширение")
//                                .substring(file.getOriginalFilename().lastIndexOf("."))
//                )
//                .toFile();
//
//        file.transferTo(saveFile);
//
//        Cloudinary cloudinary = new Cloudinary((urlKey));
//
//        Map upload = cloudinary.uploader().upload(saveFile, ObjectUtils.asMap("resource_type", "video"));
//
//        return (String) upload.get("url");
//    }

    @Override
    public String saveVideo(MultipartFile file, Lesson lesson) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new FileEmptyException("Файл пустой");
        }

        RequestBody body = RequestBody.create(
                file.getBytes(),
                MediaType.parse(Objects.requireNonNull(file.getContentType()))
        );

        Request request = new Request.Builder()
                .url("https://uploader.kinescope.io/video")
                .method("POST", body)
                .addHeader("Authorization", key)
                .addHeader("X-Project-ID", getProjectIdFromKinescope(lesson))
                .addHeader("X-Video-Title", lesson.getTitle())
                .addHeader("X-Video-Description", "")
                .addHeader("X-File-Name", file.getOriginalFilename())
                .build();

        Response response = executeRequest(request);
        JSONObject jsonResponse = new JSONObject(response.body().string());
        return jsonResponse.getJSONObject("data").getString("embed_link");
    }

    @Override
    public ResponseEntity<String> saveVideoForLesson(Long lessonId, String videoUrl, MultipartFile video, User user) throws IOException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок с таким айди не найден"));

        if (!user.getEmail().equals(lesson.getModule().getCourse().getOwner().getEmail())) {
            throw new NoAccessException("У вас нет доступа к изменению данного курса");
        }

        if (lesson.getLessonType() != LessonType.VIDEO) {
            throw new NoAccessException("Это не видеоурок");
        }

        if (lesson.getModule().getCourse().getCourseType() == CourseType.PAID) {
            lesson.setVideoUrl(saveVideo(video, lesson));
        } else if (videoUrl == null) {
            return ResponseEntity.badRequest().body("VideoUrl не должен быть пустым");
        } else {
            lesson.setVideoUrl(videoUrl);
        }

        lessonRepository.save(lesson);
        return ResponseEntity.ok("Видео успешно сохранено");
    }

    private String getProjectIdFromKinescope(Lesson lesson) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.kinescope.io/v1/projects?per_page=100&catalog_type=vod")
                .method("GET", null)
                .addHeader("Authorization", key)
                .build();

        Response response = executeRequest(request);
        JSONObject jsonResponse = new JSONObject(response.body().string());
        JSONArray data = jsonResponse.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {
            JSONObject project = data.getJSONObject(i);
            if (project.getString("name").equals("Course_" + lesson.getModule().getCourse().getId())) {
                return project.getString("id");
            }
        }

        String jsonBody = "{\"name\": \"Course_" + lesson.getModule().getCourse().getId() + "\"}";
        request = new Request.Builder()
                .url("https://api.kinescope.io/v1/projects")
                .method("POST", RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", key)
                .build();
        response = executeRequest(request);
        jsonResponse = new JSONObject(response.body().string());

        return jsonResponse.getJSONObject("data").getString("id");
    }

    private Response executeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }
}
