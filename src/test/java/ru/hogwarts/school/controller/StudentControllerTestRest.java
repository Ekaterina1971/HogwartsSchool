package ru.hogwarts.school.controller;


import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.URI;

import static jdk.dynalink.linker.support.Guards.isNotNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isNotNull;

@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentControllerTestRest {
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
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("blue");
        faculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Filip");
        student.setAge(19);
        //student.setId(1L);
        student.setFaculty(faculty);

        ResponseEntity<Student> response = this.testRestTemplate.postForEntity(
                getAddress(),
                student,
                Student.class);


        Student createdStudent = response.getBody();
        assertThat(createdStudent).isNotNull();
        assertThat(createdStudent.getName()).isEqualTo("Filip");
        assertThat(createdStudent.getAge()).isEqualTo(19);
        assertThat(createdStudent.getFaculty().getId()).isEqualTo(faculty.getId());
        assertThat(createdStudent.getFaculty().getName()).isEqualTo("Hufflepuff");

    }

    @Test
    public void editStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("blue");
        Faculty savedFaculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Filimon");
        student.setAge(19);
        student.setFaculty(savedFaculty);
        Student savedStudent = studentRepository.save(student);

        assertThat(savedStudent.getId()).isGreaterThan(0);

        savedStudent.setName("Rovena");

        this.testRestTemplate.put(getAddress(),
                         savedStudent.getId(),
                         savedStudent);

        ResponseEntity<Student> editStudent = this.testRestTemplate.postForEntity(
                getAddress(),
                savedStudent.getId(),
                Student.class);

        assertThat(editStudent).isNotNull();
        assertThat(editStudent.getBody().getName()).isEqualTo(savedStudent.getName());
    }


    @Test
    public void deleteStudentTest() {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(15);


        studentRepository.save(student);

        ResponseEntity<Student> responseDelete = testRestTemplate.exchange(
                getAddress() + "/" + studentRepository.findAll().get(0).getId(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }
    @Test
    public void findAllStudentTest(){
       // Student student = new Student();
       // student.setName("Vera");
      //  student.setAge(13);

        //Student student1 = new Student();
        //student1.setName("Ivan");
       // student1.setAge(14);

       // studentRepository.save(student);
       // studentRepository.save(student1);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress() + "/" + studentRepository.findAll(),
                HttpMethod.GET,
                null,
                Student.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

}











