package br.com.devdojo.examgenerator.endpoint.v1.question;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
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
@RequestMapping("v1/professor/course/question")
@Api(description = "Operations related to courses' question")
public class QuestionEndpoint {
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public QuestionEndpoint(QuestionRepository questionRepository,
                            CourseRepository courseRepository, GenericService service,
                            CascadeDeleteService deleteService, EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "Return a question based on it's id", response = Question.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(questionRepository.findOne(id));
    }

    @ApiOperation(value = "Return a list of question related to course", response = Question[].class)
    @GetMapping(path = "list/{courseId}/")
    public ResponseEntity<?> listQuestions(@PathVariable long courseId,
                                           @ApiParam("Question title") @RequestParam(value = "title", defaultValue = "") String title) {
        return new ResponseEntity<>(questionRepository.listQuestionsByCourseAndTitle(courseId, title), OK);
    }

    @ApiOperation(value = "Delete a specific question and all related choices and return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long id) {
        validateQuestionExistenceOnDB(id);
        deleteService.deleteQuestionAndAllRelatedEntities(id);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Update question and return 200 Ok with no body")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Question question) {
        validateQuestionExistenceOnDB(question.getId());
        questionRepository.save(question);
        return new ResponseEntity<>(OK);
    }

    private void validateQuestionExistenceOnDB(Long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, questionRepository, "Question not found");
    }

    @ApiOperation(value = "Create question and return the question created", response = Question.class)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Question question) {
        service.throwResourceNotFoundIfDoesNotExist(question.getCourse(), courseRepository, "Course not found");
        question.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(questionRepository.save(question), OK);
    }


}
