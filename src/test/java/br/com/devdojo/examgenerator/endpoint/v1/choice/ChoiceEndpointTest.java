package br.com.devdojo.examgenerator.endpoint.v1.choice;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.question.QuestionEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.questionassignment.QuestionAssignmentEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

/**
 * @author William Suane for DevDojo on 11/20/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ChoiceEndpointTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private ChoiceRepository choiceRepository;
    @MockBean
    private QuestionAssignmentRepository questionAssignmentRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Choice choiceCorrectAnswerFalse = mockChoiceCorrectAnswerFalse();
    private Choice choiceCorrectAnswerTrue = mockChoiceCorrectAnswerTrue();
    private Choice choiceWithQuestionNotAssociatedWithAssignment = mockChoiceQuestionNotAssociatedWithAssignment();

    public static Choice mockChoiceCorrectAnswerFalse() {
        return Choice.Builder.newChoice()
                .id(1L)
                .title("Is a room")
                .question(QuestionEndpointTest.mockQuestion())
                .correctAnswer(false)
                .professor(ProfessorEndpointTest.mockProfessor())
                .build();
    }

    public static Choice mockChoiceCorrectAnswerTrue() {
        return Choice.Builder.newChoice()
                .id(2L)
                .title("is a template")
                .question(QuestionEndpointTest.mockQuestion())
                .correctAnswer(true)
                .professor(ProfessorEndpointTest.mockProfessor())
                .build();
    }

    public static Choice mockChoiceQuestionNotAssociatedWithAssignment() {
        Question question = QuestionEndpointTest.mockQuestion();
        question.setId(2L);
        return Choice.Builder.newChoice()
                .id(3L)
                .title("is a template")
                .question(question)
                .correctAnswer(true)
                .professor(ProfessorEndpointTest.mockProfessor())
                .build();
    }

    @Before
    public void configProfessorHeader() {
        String body = "{\"username\":\"william\",\"password\":\"devdojo\"}";
        HttpHeaders headers = testRestTemplate.postForEntity("/login", body, String.class).getHeaders();
        this.professorHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configWrongHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "1111");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @Before
    public void setup() {
        BDDMockito.when(questionAssignmentRepository.listQuestionAssignmentByQuestionId(choiceCorrectAnswerTrue.getQuestion().getId()))
                .thenReturn(Collections.singletonList(QuestionAssignmentEndpointTest.mockQuestionAssignment()));

        BDDMockito.when(choiceRepository.findOne(choiceCorrectAnswerFalse.getId())).thenReturn(choiceCorrectAnswerFalse);
        BDDMockito.when(choiceRepository.findOne(choiceCorrectAnswerTrue.getId())).thenReturn(choiceCorrectAnswerTrue);
        BDDMockito.when(choiceRepository.findOne(choiceWithQuestionNotAssociatedWithAssignment.getId())).thenReturn(choiceWithQuestionNotAssociatedWithAssignment);
        BDDMockito.when(choiceRepository.listChoicesByQuestionId(choiceCorrectAnswerTrue.getQuestion().getId()))
                .thenReturn(asList(choiceCorrectAnswerFalse, choiceCorrectAnswerTrue));
        BDDMockito.when(questionRepository.findOne(choiceCorrectAnswerTrue.getQuestion().getId()))
                .thenReturn(choiceCorrectAnswerTrue.getQuestion());
    }

    @Test
    public void getChoiceByIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/1", GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listChoicesByQuestionIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/1/", GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listChoicesByQuestionIdWhenQuestionIdDoesNotExistsShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Choice>> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/99/", GET,
                professorHeader, new ParameterizedTypeReference<List<Choice>>() {
                });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listChoicesByQuestionIdWhenQuestionIdExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/1/", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getChoiceByIdWithoutIdShouldReturn400() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getChoiceByIdWhenChoiceIdDoesNotExistsShouldReturn404() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/-1", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getChoiceByIdWhenChoiceIdExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/1", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteChoiceWhenIdExistsAndItsQuestionIsNotInAnyAssignmentShouldReturn200() throws Exception {
        long id = 3L;
        BDDMockito.doNothing().when(choiceRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteChoiceWhenIdExistsAndItsQuestionIsInAnyAssignmentShouldReturn409() throws Exception {
        long id = 1L;
        BDDMockito.doNothing().when(choiceRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(409);
    }

    @Test
    public void deleteChoiceWhenIdDoesNotExistsShouldReturn404() throws Exception {
        long id = -1L;
        BDDMockito.doNothing().when(choiceRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createChoiceWhenTitleIsNullShouldReturn400() throws Exception {
        Choice choice = choiceRepository.findOne(1L);
        choice.setTitle(null);
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void createChoiceWhenQuestionDoesNotExistsShouldReturn404() throws Exception {
        Choice choice = choiceRepository.findOne(1L);
        choice.setQuestion(new Question());
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createChoiceWhenEverythingIsRightShouldReturn200() throws Exception {
        Choice choice = choiceRepository.findOne(1L);
        choice.setId(null);
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    public void createChoiceWithCorrectAnswerTrueShouldTriggerUpdateChoicesAndCreate() throws Exception {
        Choice choice = choiceRepository.findOne(2L);
        choice.setId(null);
        createChoice(choice);
        BDDMockito.verify(choiceRepository, Mockito.times(1)).save(choice);
        BDDMockito.verify(choiceRepository,
                Mockito.times(1))
                .updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
    }

    private ResponseEntity<String> createChoice(Choice choice) {
        BDDMockito.when(choiceRepository.save(choice)).thenReturn(choice);
        return testRestTemplate.exchange("/v1/professor/course/question/choice/", POST,
                new HttpEntity<>(choice, professorHeader.getHeaders()), String.class);
    }
}