package ru.hogwarts.school.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> facultyColor(String color) {
        return facultyRepository.findAll().stream().filter
                (faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
    }

    public Faculty findByNameIgnoreCaseAndColorIgnoreCase(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(name, color);
    }
}
    //public Collection<Faculty> findByColor(String color) {
       // ArrayList<Faculty> result = new ArrayList<>();
      //  for (Faculty faculty : facultyRepository.save() {
          //  if (Objects.equals(faculty.getColor(), color)) {
          //    //  result.add(faculty);
          //  }
      //  }
      //  return result;
   // }

