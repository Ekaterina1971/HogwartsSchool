package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("The student has been added to the application");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("findStudent method has been invoked");
        logger.debug("Requesting info for student with id: {}, id");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("editStudent method has been invoked");
        logger.error("There is no student with id");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("The student of ID has been deleted");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        logger.info("getAllStudent method has been invoked");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween (int fromAge, int toAge) {
        return studentRepository.findByAgeBetween(fromAge, toAge);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    public Long countAllStudents() {
        return studentRepository.countAllStudents();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public Collection<Student> findFiveLast() {
        return studentRepository.findFiveLast();
    }
}
