package br.com.devdojo.examgenerator.endpoint.v1.questionassignment;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.assignment.AssignmentEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.choice.ChoiceEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.course.CourseEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.question.QuestionEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.*;
import br.com.devdojo.examgenerator.persistence.respository.AssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionAssignmentRepository;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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
 * @author William Suane on 11/02/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionAssignmentEndpointTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private AssignmentRepository assignmentRepository;
    @MockBean
    private QuestionAssignmentRepository questionAssignmentRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Question question = QuestionEndpointTest.mockQuestion();
    private Course course = CourseEndpointTest.mockCourse();
    private Assignment assignment = AssignmentEndpointTest.mockAssignment();
    private Choice choice1 = ChoiceEndpointTest.mockChoiceCorrectAnswerTrue();
    private Choice choice2 = ChoiceEndpointTest.mockChoiceCorrectAnswerFalse();
    private QuestionAssignment questionAssignment = mockQuestionAssignment();

    public static QuestionAssignment mockQuestionAssignment() {
        return QuestionAssignment.Builder.newQuestionAssignment()
                .assignment(AssignmentEndpointTest.mockAssignment())
                .professor(ProfessorEndpointTest.mockProfessor())
                .question(QuestionEndpointTest.mockQuestion())
                .grade(50)
                .id(1L)
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
        question.setChoices(asList(choice1, choice2));
        BDDMockito.when(questionRepository.listQuestionsByCourseNotAssociatedWithAnAssignment(course.getId(), assignment.getId())).thenReturn(Collections.singletonList(question));
        BDDMockito.when(questionRepository.findOne(question.getId())).thenReturn(question);
        BDDMockito.when(assignmentRepository.findOne(assignment.getId())).thenReturn(assignment);
        BDDMockito.when(questionAssignmentRepository.findOne(questionAssignment.getId())).thenReturn(questionAssignment);
    }

    @Test
    public void listValidQuestionsForAnAssignmentWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/{courseId}/{assignmentId}",
                GET, wrongHeader, String.class, course.getId(), assignment.getId());
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listValidQuestionsForAnAssignmentWhenCourseDoesNotExistShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Question>> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/-1/1", GET,
                professorHeader, new ParameterizedTypeReference<List<Question>>() {
                });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listValidQuestionsForAnAssignmentWhenAssignmentDoesNotExistShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Question>> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/1/-1", GET,
                professorHeader, new ParameterizedTypeReference<List<Question>>() {
                });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listValidQuestionsForAnAssignmentWhenAssignmentAndCourseExistAndHasValidQuestionsShouldReturnListWithOneElement() throws Exception {
        ResponseEntity<List<Question>> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/1/1", GET,
                professorHeader, new ParameterizedTypeReference<List<Question>>() {
                });
        assertThat(exchange.getBody().size()).isEqualTo(1);
    }

    @Test
    public void deleteQuestionAssignmentWhenIdExistsShouldReturn200() throws Exception {
        long id = questionAssignment.getId();
        BDDMockito.doNothing().when(questionAssignmentRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void createQuestionAssignmentWhenEverythingIsRightShouldReturn200() throws Exception {
        QuestionAssignment questionAssignment = mockQuestionAssignment();
        questionAssignment.setId(null);
        assertThat(createQuestionAssignment(questionAssignment).getStatusCodeValue()).isEqualTo(200);
    }

    private ResponseEntity<String> createQuestionAssignment(QuestionAssignment questionAssignment) {
        BDDMockito.when(questionAssignmentRepository.save(questionAssignment)).thenReturn(questionAssignment);
        return testRestTemplate.exchange("/v1/professor/course/assignment/questionassignment/", POST,
                new HttpEntity<>(questionAssignment, professorHeader.getHeaders()), String.class);
    }

}