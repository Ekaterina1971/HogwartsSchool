package ru.hogwarts.school.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("addFaculty method has been invoked");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("findFaculty method has been invoked");
        logger.debug("Requesting info for faculty with id: {}, id");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("editFaculty method has been invoked");
        logger.error("There is no faculty with id");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("deleteFaculty method has been invoked");
        facultyRepository.deleteById(id);
    }

    public List<Faculty> facultyColor(String color) {
        logger.info("facultyColor method has been invoked");
        return facultyRepository.findAll().stream().filter
                (faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
    }

    public Faculty findFacultyByNameOrColor(String name, String color) {
        logger.info("findFacultyByNameColor method has been invoked");
        return facultyRepository.findFacultyByNameOrColor(name, color);
    }

    public Optional<Faculty> findByFacultyId(Long facultyId) {
        logger.info("findByFacultyId method has been invoked");
        return facultyRepository.findById(facultyId);
    }

    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }
}


