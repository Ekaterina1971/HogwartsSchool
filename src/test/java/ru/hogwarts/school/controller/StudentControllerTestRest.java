package ru.hogwarts.school.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles
public class StudentControllerTestRest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    private StudentController studentController;

    private String getAddress() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    public void createStudentTest() throws Exception{
        Student student = new Student();
        student.setName("Filimon");
        student.setAge(19);
        student.setId(1L);

        assertNotNull(this.testRestTemplate.postForObject(getAddress(),
                student,
                Student.class));

    }
    @Test
    public void editStudent() throws Exception {
        Student student = new Student();
        student.setName("Filimon");
        student.setAge(19);

        studentRepository.save(student);

        Student student1 = new Student();
        student1.setName("Poll");
        student1.setAge(16);

        RequestEntity<Student> request = new RequestEntity<>(student1, HttpMethod.PUT, URI.create(getAddress()));

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.PUT,
                request,
                Student.class
        );

    }



}






