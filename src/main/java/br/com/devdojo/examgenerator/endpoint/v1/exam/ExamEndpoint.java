package br.com.devdojo.examgenerator.endpoint.v1.exam;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
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

import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 04/13/18.
 */
@RestController
@RequestMapping("v1/student")
@Api(description = "Operations to associate questions to an assignment")
public class ExamEndpoint {
    private final QuestionRepository questionRepository;
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private final EndpointUtil endpointUtil;

    @Autowired
    public ExamEndpoint(QuestionRepository questionRepository,
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


    @ApiOperation(value = "List all Questions from QuestionAssignment by the assignment access code", response = Question[].class)
    @GetMapping(path = "questions/{accessCode}")
    public ResponseEntity<?> listQuestionsFromQuestionAssignmentByAssignmentAccessCode(@PathVariable long accessCode) {
        return new ResponseEntity<>(questionAssignmentRepository.listQuestionsFromQuestionAssignmentByAssignmentAccessCode(accessCode), OK);
    }


}

