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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    StudentService studentService;

    @SpyBean
    FacultyService facultyService;

    @InjectMocks
    StudentController studentController;

    @Test
    void createStudentTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("blue");
        faculty = facultyRepository.save(faculty);

        Student student = new Student();
        student.setName("Filip");
        student.setAge(19);
        //student.setId(1L);
        student.setFaculty(faculty);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", "Filip");
        studentObject.put("age", "19");

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Filip"))
                .andExpect(jsonPath("$.age").value("19"));

    }

    @Test
    public void editStudentTest() throws Exception{
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(18);
        student.setId(1L);

        studentRepository.save(student);

        Student editStudent = new Student(5L,"Rovena", 20);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", editStudent.getId());
        jsonObject.put("name", editStudent.getName());
        jsonObject.put("age", editStudent.getAge());

        when(studentService.editStudent(any(Student.class))).thenReturn(editStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(editStudent);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student" + editStudent.getId())
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(editStudent.getId()))
                .andExpect(jsonPath("$.name").value(editStudent.getName()))
                .andExpect(jsonPath("$.age").value(editStudent.getAge()));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        long id = 1L;
        Student student = new Student();
        student.setName("Oleg");
        student.setAge(18);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        mockMvc.perform(delete("/student?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void getAllStudentTest() throws Exception {
        List<Student> testedList = new ArrayList<>(List.of(
                new Student(1L, "testedName1", 20),
                new Student(3L, "testedName2", 20),
                new Student(2L, "testedName3", 20)));
        when(studentRepository.findAll()).thenReturn(testedList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFacultyByStudentIDTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Hogwarts");
        faculty.setColor("green");

        Student student = new Student();
        student.setName("Oleg");
        student.setAge(17);
        student.setFaculty(faculty);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/get/faculty/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    public void findByAgeBetweenTest() throws Exception {
        Student student1 = new Student(1L, "Filip", 25);
        Student student2 = new Student(2L, "Zoy", 32);

        List<Student> studentList = new ArrayList<>(Arrays.asList(student1, student2));

        int fromAge = 20;
        int toAge = 30;

        when(studentRepository.findByAgeBetween(fromAge,toAge)).thenReturn(studentList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age")
                        .param("fromAge", "20")
                        .param("toAge", "30")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

    }
}

