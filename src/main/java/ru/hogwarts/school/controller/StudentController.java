package ru.hogwarts.school.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public Collection<Student> findAll() {
        return studentService.getAllStudent();
    }

    @GetMapping("byAgeBetween")
    public Collection<Student> findByAgeBetween(@RequestParam int fromAge,
                                                @RequestParam int toAge) {
        return studentService.findByAgeBetween(fromAge, toAge);
    }

    @GetMapping("/{studentId}/faculty")
    public String getFacultyByStudentId(@PathVariable Long studentId) {
        return studentService.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null).toString();
    }
    @GetMapping("count-students")
    public ResponseEntity<Long> countAllStudents() {
        return ResponseEntity.ok(studentService.countAllStudents());
    }

    @GetMapping("get-average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("find-five-last")
    public ResponseEntity<Collection<Student>> findFiveLast() {
        return ResponseEntity.ok(studentService.findFiveLast());
    }
}
