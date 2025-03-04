package ru.hogwarts.school.controller;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

//import static jdk.internal.jimage.ImageReaderFactory.get;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void createFaculty() throws Exception {
        Long id = 1L;
        String name = "Pixel";
        String color = "with";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void getFaculty() throws Exception {
        Long id = 1L;
        String name = "Pixel";
        String color = "with";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFaculty() throws Exception {
        Long id = 1L;
        String name = "Pixel";
        String newName = "Mixel";
        String color = "with";
        String newColor = "red";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(newName);
        faculty.setColor(newColor);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    void deleteFaculty() throws Exception {
        long id = 1L;
        String name = "Pixel";
        String color = "Yellow";
        Faculty faculty = new Faculty(id, name, color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findNameOrColorTest() throws Exception {

        when(facultyRepository.findFacultyByNameOrColor(anyString(), anyString()))
                .thenReturn((Faculty)List.of(
                        new Faculty(1L, "name1", "color1"),
                        new Faculty(2L, "name2", "color2")));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/byColorOrName?name=name1&color=color2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[0].color").value("color1"))
                .andExpect(jsonPath("$[1].name").value("name2"))
                .andExpect(jsonPath("$[1].color").value("color2"));
    }

    @Test
    public void getStudentsByFacultyTest() throws Exception {
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(1);

        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");
        Set<Student> students = new HashSet<>();
        students.add(student);
        faculty.setStudents(students);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-student")
                        .param("studentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()));
    }
}

