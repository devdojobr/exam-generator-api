package br.com.devdojo.examgenerator.endpoint.v1.course;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository, GenericService service,
                          CascadeDeleteService deleteService, EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.service = service;
        this.deleteService = deleteService;
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

    @ApiOperation(value = "Delete a specific course and all related questions and choices and return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long id) {
        validateCourseExistenceOnDB(id);
        deleteService.deleteCourseAndAllRelatedEntities(id);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Update course and return 200 Ok with no body")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Course course) {
        validateCourseExistenceOnDB(course.getId());
        courseRepository.save(course);
        return new ResponseEntity<>(OK);
    }

    private void validateCourseExistenceOnDB(Long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
    }

    @ApiOperation(value = "Create course and return the course created")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), OK);
    }


}
