package br.com.devdojo.examgenerator.endpoint.v1.choice;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.exception.ConflictException;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import br.com.devdojo.examgenerator.persistence.respository.ChoiceRepository;
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

import static org.springframework.http.HttpStatus.OK;

/**
 * @author William Suane for DevDojo on 11/20/17.
 */
@RestController
@RequestMapping("v1/professor/course/question/choice")
@Api(description = "Operations related to questions' choice")
public class ChoiceEndpoint {
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final QuestionAssignmentRepository questionAssignmentRepository;
    private final GenericService service;
    private final EndpointUtil endpointUtil;

    @Autowired
    public ChoiceEndpoint(QuestionRepository questionRepository,
                          ChoiceRepository choiceRepository, QuestionAssignmentRepository questionAssignmentRepository, GenericService service,
                          EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.questionAssignmentRepository = questionAssignmentRepository;
        this.service = service;
        this.endpointUtil = endpointUtil;
    }
    @ApiOperation(value = "Return a choice based on it's id", response = Choice.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getChoiceById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(choiceRepository.findOne(id));
    }

    @ApiOperation(value = "Return a list of choices related to the questionId", response = Choice[].class)
    @GetMapping(path = "list/{questionId}/")
    public ResponseEntity<?> listChoicesByQuestionId(@PathVariable long questionId) {
        return new ResponseEntity<>(choiceRepository.listChoicesByQuestionId(questionId), OK);
    }

    @ApiOperation(value = "Create choice and return the choice created",
            notes = "If this choice's correctAnswer is true all other choices' correctAnswer related to this question will be updated to false")
    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody Choice choice) {
        throwResourceNotFoundExceptionIfQuestionDoesNotExist(choice);
        choice.setProfessor(endpointUtil.extractProfessorFromToken());
        Choice savedChoice = choiceRepository.save(choice);
        updateChangingOtherChoicesCorrectAnswerToFalse(choice);
        return new ResponseEntity<>(savedChoice, OK);
    }

    @ApiOperation(value = "Update choice and return 200 Ok with no body",
            notes = "If this choice's correctAnswer is true all other choices' correctAnswer related to this question will be updated to false")
    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@Valid @RequestBody Choice choice) {
        throwResourceNotFoundExceptionIfQuestionDoesNotExist(choice);
        updateChangingOtherChoicesCorrectAnswerToFalse(choice);
        choiceRepository.save(choice);
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Delete a specific choice and return 200 Ok with no body")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, choiceRepository, "Choice was not found");
        Choice choice = choiceRepository.findOne(id);
        throwConflictExceptionIfQuestionIsBeingUsedInAnyAssignment(choice.getQuestion().getId());
        choiceRepository.delete(id);
        return new ResponseEntity<>(OK);
    }

    private void throwConflictExceptionIfQuestionIsBeingUsedInAnyAssignment(long questionId) {
        List<QuestionAssignment> questionAssignments = questionAssignmentRepository.listQuestionAssignmentByQuestionId(questionId);
        if (questionAssignments.isEmpty()) return;
        String assignments = questionAssignments
                .stream()
                .map(qa -> qa.getAssignment().getTitle())
                .collect(Collectors.joining(", "));
        throw new ConflictException("This choice cannot be deleted because this question is being used in the following assignments: " + assignments);
    }

    private void throwResourceNotFoundExceptionIfQuestionDoesNotExist(@Valid @RequestBody Choice choice) {
        service.throwResourceNotFoundIfDoesNotExist(choice.getQuestion(), questionRepository, "The question related to this choice was not found");
    }

    private void updateChangingOtherChoicesCorrectAnswerToFalse(Choice choice) {
        if (choice.isCorrectAnswer())
            choiceRepository.updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
    }
}
