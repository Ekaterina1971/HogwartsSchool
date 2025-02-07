package ru.hogwarts.school.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {
   // @Value("${avatars.dir.path}")
    private String avatarsDir;
    //@Autowired
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween (int fromAge, int toAge) {
        return studentRepository.findByAgeBetween(fromAge, toAge);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

   // public Avatar findAvatar(long studentId) {
       // return avatarRepository.findByStudentId(studentId).orElseThrow();
   // }

    public void uploadAvatar(Long studentId, MultipartFile avatarfile) throws IOException {
        Student student = studentRepository.findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(avatarfile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarfile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarfile.getSize());
        avatar.setMediaType(avatarfile.getContentType());
        avatar.setData(avatarfile.getBytes());

        avatarRepository.save(avatar);
    }
    private String getExtension(String fileName) {
       return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}


