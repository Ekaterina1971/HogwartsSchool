package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.NoAvatarsException;
import ru.hogwarts.school.exception.WrongIndexException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static io.swagger.v3.core.util.AnnotationsUtils.getExtensions;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
   // private final StudentService studentService;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        //this.studentService = studentService;
        this.studentRepository = studentRepository;
       //this.avatarsDir = avatarsDir;
    }
    public Avatar createAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    public Avatar readAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

    public Collection<Avatar> readAllAvatars() {
        if (avatarRepository.count() == 0) {
            throw new NoAvatarsException();
        }

        return avatarRepository.findAll();
    }

    public Avatar updateAvatar(Avatar avatar) {
        if (!avatarRepository.existsById(avatar.getId())) {
            throw new WrongIndexException();
        }

        return avatarRepository.save(avatar);
    }

    public void deleteAvatar(Long id) {
        if (!avatarRepository.existsById(id)) {
            throw new WrongIndexException();
        }
        avatarRepository.deleteById(id);
    }

   // public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException{
      //  Student student = findStudent(studentId);
//        хранит путь до директории с загружаемыми файлами.
      //  Path filePath = Path.of(String.valueOf(avatarsDir), student + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
//        Создаем нужную нам директорию для хранения данных и удаляем из нее файл, если он уже присутствует там.
    //    Files.createDirectories(filePath.getParent());
      //  Files.deleteIfExists(filePath);
      //  try (
       //             InputStream is = avatarFile.getInputStream();
       //             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
       //             BufferedInputStream bis = new BufferedInputStream(is, 1024);
       //             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
      //      ) {
         //       bis.transferTo(bos);
        //    }
       // Avatar avatar = new Avatar();
    //    avatar.setStudent(student);
     //   avatar.setFilePath(filePath.toString());
     //   avatar.setFileSize(avatarFile.getSize());
      //  avatar.setMediaType(avatarFile.getContentType());
     //   avatar.setData(avatarFile.getBytes());
     //   avatarRepository.save(avatar);
  //  }

   // private String getExtensions(String fileName) {
      //  return fileName.substring(fileName.lastIndexOf(".") + 1);
   // }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElseThrow();
    }

    public Collection<Avatar> getAvatarLimit(Integer pageNamber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNamber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}