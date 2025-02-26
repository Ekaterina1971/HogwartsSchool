package ru.hogwarts.school.controller;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.exception.model.Faculty;
import ru.hogwarts.school.exception.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.net.URI;

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

        studentRepository.save(student);

        student.setName("Rovena");
        student.setAge(17);

       RequestEntity<Student> request = new RequestEntity<>(student, HttpMethod.PUT, URI.create(getAddress()));

       testRestTemplate.put(getAddress(), student,
                         Student.class);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress(),
                HttpMethod.PUT,
                request,
                Student.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);

    }


    @Test
    public void deleteStudentTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("bluered");
        facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Oleg");
        student.setAge(15);
        student.setFaculty(faculty);

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
        Faculty faculty1 = new Faculty();
        faculty1.setName("Huffl");
        faculty1.setColor("blueyelloy");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Hufflq");
        faculty2.setColor("redyelloy");
        facultyRepository.save(faculty2);

        Student student1 = new Student();
        student1.setName("Vera");
        student1.setAge(13);
        student1.setFaculty(faculty1);

        Student student2 = new Student();
        student2.setName("Ivan");
        student2.setAge(14);
        student2.setFaculty(faculty2);

        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                getAddress() + "/" + studentRepository.findAll(),
                HttpMethod.GET,
                null,
                Student.class
        );
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void findByAgeBetweenTest(){
        Faculty faculty1 = new Faculty();
        faculty1.setName("Griff");
        faculty1.setColor("grenyelloy");
        facultyRepository.save(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("Kort");
        faculty2.setColor("redwiht");
        facultyRepository.save(faculty2);

        Student student1 = new Student();
        student1.setName("Verona");
        student1.setAge(13);
        student1.setFaculty(faculty1);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Ivaneska");
        student2.setAge(17);
        student2.setFaculty(faculty2);
        studentRepository.save(student2);

        int minAge = 15;
        int maxAge = 19;

        String result = testRestTemplate.getForObject("http://localhost:" + port + "/student/filter?min=16&max=25", String.class);
        assertThat(result).isNotNull();
    }
    @Test
    public void getFacultyByStudentIdTest() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Griff");
        faculty1.setColor("grenyelloy");
        facultyRepository.save(faculty1);

        Student student1 = new Student();
        student1.setName("Pavel");
        student1.setAge(18);
        student1.setFaculty(faculty1);
        studentController.createStudent(student1);

        Faculty actual = this.testRestTemplate.getForObject("http://localhost:"
                + port + "/student" + student1.getId() + "/faculty", Faculty.class);

        assertThat(actual.getId()).isEqualTo(new Faculty().getId());
    }
}













