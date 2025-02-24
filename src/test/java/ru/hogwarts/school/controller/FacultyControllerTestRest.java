package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.net.URI;
import java.util.List;

//import static jdk.incubator.foreign.MemoryAccess.getAddress;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.springframework.web.client.RestClientUtils.getBody;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerTestRest {
    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    FacultyService facultyService;

    @Autowired
    FacultyController facultyController;

    @Autowired
    FacultyRepository facultyRepository;

    private String getRootUrl() {
        return "http://localhost:" + port + "/faculty";
    }


    @Test
    public void createFacultyTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColor("brown");

        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(
                getRootUrl(),
                faculty,
                Faculty.class
        );
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);

    }

    @Test
    public void editFacultyTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColor("brown");

        facultyRepository.save(faculty);

        Faculty faculty1 = new Faculty();
        faculty1.setId(faculty1.getId());

        faculty1.setColor("red");
        faculty1.setName("Slytherin");

        RequestEntity<Faculty> request = new RequestEntity<>(faculty1, HttpMethod.PUT, URI.create(getRootUrl()));

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                getRootUrl(),
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty1);
    }

    @Test
    public void deleteFacultyTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Uffend");
        faculty.setColor("white");

        facultyController.createFaculty(faculty);

        testRestTemplate.delete(getRootUrl() + faculty.getId());

        assertThat(this.testRestTemplate.getForObject(getRootUrl(),
                String.class).isEmpty());

        facultyController.deleteFaculty(faculty.getId());
    }

    @Test
    public void getFacultyInfoTest() throws Exception {
        Assertions.assertThat(
                        this.testRestTemplate.getForObject(getRootUrl(),
                                String.class))
                .isNotEmpty();
    }
    @Test
    public void getFacultyByStudent() throws Exception {
        assertNotNull(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty/by-student", String.class));
    }

    @Test
    public void findByNameOrColorTest () throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        facultyController.createFaculty(faculty);

        assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty/name/" + faculty.getName(),
                String.class)).isNotNull();
        assertThat(faculty.getColor()).isEqualTo("green");

        assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty/color/" + faculty.getColor(),
                String.class)).isNotNull();
        assertThat(faculty.getName()).isEqualTo("Hogwarts");

    }

}
