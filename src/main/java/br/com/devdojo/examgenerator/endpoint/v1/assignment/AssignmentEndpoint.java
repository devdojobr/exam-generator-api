package br.com.devdojo.examgenerator.endpoint.v1.assignment;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
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
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@RestController
@RequestMapping("v1/professor/course/assignment")
@Api(description = "Operations related to courses' assignment")
public class AssignmentEndpoint {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final GenericService service;
    private final EndpointUtil endpointUtil;
    private final CascadeDeleteService deleteService;

    @Autowired
    public AssignmentEndpoint(AssignmentRepository assignmentRepository,
                              CourseRepository courseRepository, GenericService service,
                              EndpointUtil endpointUtil, CascadeDeleteService deleteService) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.service = service;
        this.endpointUtil = endpointUtil;
        this.deleteService = deleteService;
    }

    @ApiOperation(value = "Return an assignment based on it's id", response = Assignment.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(assignmentRepository.findOne(id));
    }

    @ApiOperation(value = "Return a list of assignments related to course", response = Assignment[].class)
    @GetMapping(path = "list/{courseId}/")
    public ResponseEntity<?> listAssignments(@PathVariable long courseId,
                                             @ApiParam("Assignment title") @RequestParam(value = "title", defaultValue = "") String title) {
        return new ResponseEntity<>(assignmentRepository.listAssignemntsByCourseAndTitle(courseId, title), OK);
    }

    @ApiOperation(value = "Delete a specific assignment return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long id) {
        validateAssignmentExistenceOnDB(id);
        assignmentRepository.delete(id);
        deleteService.deleteAssignmentAndAllRelatedEntities(id);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Update assignment and return 200 Ok with no body")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Assignment assignment) {
        validateAssignmentExistenceOnDB(assignment.getId());
        assignmentRepository.save(assignment);
        return new ResponseEntity<>(OK);
    }

    private void validateAssignmentExistenceOnDB(Long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, assignmentRepository, "Assignment not found");
    }

    @ApiOperation(value = "Create assignment and return the assignment created")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Assignment assignment) {
        service.throwResourceNotFoundIfDoesNotExist(assignment.getCourse(), courseRepository, "Course not found");
        assignment.setProfessor(endpointUtil.extractProfessorFromToken());
        assignment.setAccessCode(generateAccessCode(assignment.getCourse().getId()));
        return new ResponseEntity<>(assignmentRepository.save(assignment), OK);
    }

    private String generateAccessCode(long courseId) {
        long accessCode = ThreadLocalRandom.current().nextLong(1000, 10000);
        while (assignmentRepository.accessCodeExistsForCourse(String.valueOf(accessCode), courseId) != null) {
            generateAccessCode(courseId);
        }
        return String.valueOf(accessCode);
    }


}
