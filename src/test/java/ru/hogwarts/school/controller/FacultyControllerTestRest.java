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
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.net.URI;

//import static jdk.incubator.foreign.MemoryAccess.getAddress;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SchoolApplication.class)
//@ActiveProfiles("test")
public class FacultyControllerTestRest {
    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    FacultyService facultyService;

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

        faculty.setColor("red");
        faculty.setName("Slytherin");

        RequestEntity<Faculty> request = new RequestEntity<>(faculty, HttpMethod.PUT, URI.create(getRootUrl()));

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
                .isEqualTo(faculty);
    }

    @Test
    public void deleteFacultyTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColor("brown");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> responseDelete = testRestTemplate.exchange(
                getRootUrl() + "/" + facultyRepository.findAll().get(9).getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }

    @Test
    public void getFacultyInfoTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Griffindor");
        faculty.setColor("brown");
        faculty.setId(2);

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                getRootUrl() + "/" + facultyRepository.findById(faculty.getId()).get(),
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }




}
