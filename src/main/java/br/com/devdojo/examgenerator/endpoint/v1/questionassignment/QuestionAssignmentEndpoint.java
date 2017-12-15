package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author William Suane for DevDojo on 12/15/17.
 */
@RestController
@RequestMapping("v1/professor/course/assignment/questionassignment")
@Api(description = "Operations to associate questions to an assignment")
public class QuestionAssignmentEndpoint {
    private final QuestionRepository questionRepository;
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final CourseRepository courseRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public QuestionAssignmentEndpoint(QuestionRepository questionRepository,
                                      QuestionAssignmentRepository questionAssignmentRepository, CourseRepository courseRepository, GenericService service,
                                      CascadeDeleteService deleteService, EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.questionAssignmentRepository = questionAssignmentRepository;
        this.courseRepository = courseRepository;
        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "Return valid questions for that course (valid questions are questions with at least two choices" +
            " and one of the choices is correct and it is not already associated with that assignment)", response = Question[].class)
    @GetMapping(path = "{courseId}/{assignmentId}")
    public ResponseEntity<?> getQuestionById(@PathVariable long courseId, @PathVariable long assignmentId) {
        List<Question> questions = questionRepository.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
        List<Question> validQuestions = questions
                .stream()
                .filter(question -> hasMoreThanOneChoice(question) && hasOnlyOneCorrectAnswer(question))
                .collect(Collectors.toList());
        return endpointUtil.returnObjectOrNotFound(validQuestions);
    }

    private boolean hasOnlyOneCorrectAnswer(Question question) {
        return question.getChoices().stream().filter(Choice::isCorrectAnswer).count() == 1;
    }

    private boolean hasMoreThanOneChoice(Question question) {
        return question.getChoices().size() > 1;
    }


}

