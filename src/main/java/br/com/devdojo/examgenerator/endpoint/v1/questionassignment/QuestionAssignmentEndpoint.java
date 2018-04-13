package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 12/15/17.
 */
@RestController
@RequestMapping("v1/professor/course/assignment/questionassignment")
@Api(description = "Operations to associate questions to an assignment")
public class QuestionAssignmentEndpoint {
    private final QuestionRepository questionRepository;
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public QuestionAssignmentEndpoint(QuestionRepository questionRepository,
                                      QuestionAssignmentRepository questionAssignmentRepository,
                                      AssignmentRepository assignmentRepository,
                                      GenericService service,
                                      CascadeDeleteService deleteService,
                                      EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.questionAssignmentRepository = questionAssignmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "Return valid questions for that course (valid questions are questions with at least two choices" +
            " and one of the choices is correct and it is not already associated with that assignment)", response = Question[].class)
    @GetMapping(path = "{courseId}/{assignmentId}")
    public ResponseEntity<?> listValidQuestionsForAnAssignment(@PathVariable long courseId, @PathVariable long assignmentId) {
        List<Question> questions = questionRepository.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
        List<Question> validQuestions = questions
                .stream()
                .filter(question -> hasMoreThanOneChoice(question) && hasOnlyOneCorrectAnswer(question))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validQuestions, OK);
    }

    private boolean hasOnlyOneCorrectAnswer(Question question) {
        return question.getChoices().stream().filter(Choice::isCorrectAnswer).count() == 1;
    }

    private boolean hasMoreThanOneChoice(Question question) {
        return question.getChoices() != null && question.getChoices().size() > 1;
    }

    @ApiOperation(value = "Associate a question to an assignment and return the QuestionAssignment created", response = QuestionAssignment[].class)
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody QuestionAssignment questionAssignment) {
        validateQuestionAndAssignmentExistence(questionAssignment);
        if (isQuestionAlreadyAssociatedWithAssignment(questionAssignment)) {
            return new ResponseEntity<>(NOT_MODIFIED);
        }
        questionAssignment.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(questionAssignmentRepository.save(questionAssignment), OK);
    }

    private void validateQuestionAndAssignmentExistence(@Valid @RequestBody QuestionAssignment questionAssignment) {
        service.throwResourceNotFoundIfDoesNotExist(questionAssignment.getQuestion(), questionRepository, "Question not found");
        service.throwResourceNotFoundIfDoesNotExist(questionAssignment.getAssignment(), assignmentRepository, "Assignment not found");
    }

    private boolean isQuestionAlreadyAssociatedWithAssignment(QuestionAssignment questionAssignment) {
        long questionId = questionAssignment.getQuestion().getId();
        long assignmentId = questionAssignment.getAssignment().getId();
        List<QuestionAssignment> questionAssignments = questionAssignmentRepository.listQuestionAssignmentByQuestionAndAssignment(questionId, assignmentId);
        return !questionAssignments.isEmpty();
    }

    @ApiOperation(value = "Delete a specific question assigned to an assignment and return 200 Ok with no body")
    @DeleteMapping(path = "{questionAssignmentId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long questionAssignmentId) {
        validateQuestionAssignmentOnDB(questionAssignmentId);
        deleteService.deleteQuestionAssignmentAndAllRelatedEntities(questionAssignmentId);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Update QuestionAssignment and return 200 Ok with no body")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody QuestionAssignment questionAssignment) {
        validateQuestionAssignmentOnDB(questionAssignment.getId());
        questionAssignmentRepository.save(questionAssignment);
        return new ResponseEntity<>(OK);
    }

    private void validateQuestionAssignmentOnDB(Long questionAssignmentId) {
        service.throwResourceNotFoundIfDoesNotExist(questionAssignmentId, questionAssignmentRepository, "QuestionAssignment not found");

    }

    @ApiOperation(value = "List all QuestionAssignment associated with assignmentId", response = QuestionAssignment[].class)
    @GetMapping(path = "{assignmentId}")
    public ResponseEntity<?> list(@PathVariable long assignmentId) {
        return new ResponseEntity<>(questionAssignmentRepository.listQuestionAssignmentByAssignmentId(assignmentId), OK);
    }


}

