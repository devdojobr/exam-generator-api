package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@RestController
@RequestMapping("v1/professor/course/assignment/questionassignment")
@Api(description = "Operations related to courses' question")
public class QuestionAssignmentEndpoint {
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final QuestionRepository questionRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public QuestionAssignmentEndpoint(QuestionAssignmentRepository questionAssignmentRepository, AssignmentRepository assignmentRepository, QuestionRepository questionRepository, GenericService service,
                                      CascadeDeleteService deleteService, EndpointUtil endpointUtil) {
        this.questionAssignmentRepository = questionAssignmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.questionRepository = questionRepository;

        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "List all available and valid questions (questions with at least 2 choices and one correct choice not assigned to an assignment)", response = Question[].class)
    @GetMapping(path = "/{courseId}/{assignmentId}")
    public ResponseEntity<?> getAvailableValidQuestions(@PathVariable long courseId, @PathVariable long assignmentId) {
        List<Question> questions = questionRepository.listQuestionsByCourseWithoutOnAssignment(courseId, assignmentId, endpointUtil.extractProfessorFromToken());
        List<Question> availableQuestions = questions
                .stream()
                .filter(question -> hasMoreThanOneChoice(question) && hasOnlyOneCorrectAnswer(question))
                .collect(toList());
        return endpointUtil.returnObjectOrNotFound(availableQuestions);
    }

    private boolean hasMoreThanOneChoice(Question question) {
        return question.getChoices().size() > 1;
    }

    private boolean hasOnlyOneCorrectAnswer(Question question) {
        return question.getChoices().stream().filter(Choice::isCorrectAnswer).count() == 1;
    }

}
