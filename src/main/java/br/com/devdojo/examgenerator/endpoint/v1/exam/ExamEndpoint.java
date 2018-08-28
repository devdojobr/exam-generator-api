package br.com.devdojo.examgenerator.endpoint.v1.exam;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 04/13/18.
 */
@RestController
@RequestMapping("v1/student/exam")
@Api(description = "Operations to associate questions to an assignment")
public class ExamEndpoint {
    private final QuestionRepository questionRepository;
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final ChoiceRepository choiceRepository;
    private final AssignmentRepository assignmentRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public ExamEndpoint(QuestionRepository questionRepository,
                        QuestionAssignmentRepository questionAssignmentRepository,
                        ChoiceRepository choiceRepository, AssignmentRepository assignmentRepository,
                        GenericService service,
                        CascadeDeleteService deleteService,
                        EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.questionAssignmentRepository = questionAssignmentRepository;
        this.choiceRepository = choiceRepository;
        this.assignmentRepository = assignmentRepository;
        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }


    @ApiOperation(value = "List all Choices based on the Questions by the assignment access code", response = Choice[].class)
    @GetMapping(path = "choice/{accessCode}")
    public ResponseEntity<?> listQuestionsFromQuestionAssignmentByAssignmentAccessCode(@PathVariable String accessCode) {
        List<Question> questions = questionAssignmentRepository.listQuestionsFromQuestionAssignmentByAssignmentAccessCode(accessCode);
        if (questions.isEmpty()) throw new ResourceNotFoundException("Invalid access code");
        List<Long> questionsId = questions.stream().map(Question::getId).collect(Collectors.toList());
        List<Choice> choices = choiceRepository.listChoicesByQuestionsIdForStudent(questionsId);
        return new ResponseEntity<>(choices, OK);
    }

    @ApiOperation(value = "Save Student's answers")
    @PostMapping(path = "{accessCode}")
    public ResponseEntity<?> save(@PathVariable String accessCode, @RequestBody Map<Long, Long> questionChoiceIdsMap) {
        Assignment assignment = assignmentRepository.accessCodeExists(accessCode);
        if (assignment == null) throw new ResourceNotFoundException("Invalid access code");
        System.out.println(questionChoiceIdsMap);
        return new ResponseEntity<>(OK);
    }


}

