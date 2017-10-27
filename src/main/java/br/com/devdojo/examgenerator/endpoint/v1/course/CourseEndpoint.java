package br.com.devdojo.examgenerator.endpoint.v1.course;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@RestController
@RequestMapping("v1/professor/course")
@Api(description = "Operations related to professors' course")
public class CourseEndpoint {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @ApiOperation(value = "Return a course based on it's id", response = Course.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCourseById(@PathVariable long id, Authentication authentication) {
        Professor professor = ((ApplicationUser) authentication.getPrincipal()).getProfessor();
        return new ResponseEntity<>(courseRepository.findByIdAndProfessor(id, professor), HttpStatus.OK);
    }
}
