package br.com.devdojo.examgenerator.endpoint.v1.course;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@RestController
@RequestMapping("v1/professor/course")
@Api(description = "Operations related to professors' course")
public class CourseEndpoint {
    private final CourseRepository courseRepository;
    private final GenericService service;
    private final EndpointUtil endpointUtil;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository,
                          GenericService service,
                          EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.service = service;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "Return a course based on it's id", response = Course.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCourseById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(courseRepository.findOne(id));
    }

    @ApiOperation(value = "Return a list of courses related to professor", response = Course.class)
    @GetMapping(path = "list")
    public ResponseEntity<?> listCourses(@ApiParam("Course name") @RequestParam(value = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(courseRepository.listCoursesByName(name), OK);
    }

    @ApiOperation(value = "Delete a specific course and return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
        courseRepository.delete(id);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Update course and return 200 Ok with no body")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Course course) {
        service.throwResourceNotFoundIfDoesNotExist(course, courseRepository, "Course not found");
        courseRepository.save(course);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Create course and return the course created")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), OK);
    }


}
