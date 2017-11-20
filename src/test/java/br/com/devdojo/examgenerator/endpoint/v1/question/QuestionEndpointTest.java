package br.com.devdojo.examgenerator.endpoint.v1.question;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.course.CourseEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Question;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * @author William Suane for DevDojo on 11/10/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionEndpointTest {
    @MockBean
    private QuestionRepository questionRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Question question = mockQuestion();

    public static Question mockQuestion() {
        return Question.Builder.newQuestion()
                .id(1L)
                .title("What is class?")
                .course(CourseEndpointTest.mockCourse())
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
        BDDMockito.when(questionRepository.findOne(question.getId())).thenReturn(question);
        BDDMockito.when(questionRepository.listQuestionsByCourseAndTitle(question.getCourse().getId(), "")).thenReturn(Collections.singletonList(question));
        BDDMockito.when(questionRepository.listQuestionsByCourseAndTitle(question.getCourse().getId(), "What is class")).thenReturn(Collections.singletonList(question));
    }

    @Test
    public void getQuestionByIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1", GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listQuestionsByCourseAndTitleWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/list/1/?title=", GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listAllQuestionsByCourseAndTitleWhenTitleDoesNotExistsShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Question>> exchange = testRestTemplate.exchange("/v1/professor/course/question/list/1/?title=xaxa", GET,
                professorHeader, new ParameterizedTypeReference<List<Question>>() {
                });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listAllQuestionsByCourseWhenTitleExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/list/1/?title=what", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getQuestionByIdWithoutIdShouldReturn400() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getQuestionByIdWhenQuestionIdDoesNotExistsShouldReturn404() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/-1", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getQuestionByIdWhenQuestionExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/1", GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteQuestionWhenIdExistsShouldReturn200() throws Exception {
        long id = 1L;
        BDDMockito.doNothing().when(questionRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteQuestionWhenIdDoesNotExistsShouldReturn404() throws Exception {
        long id = -1L;
        BDDMockito.doNothing().when(questionRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/{id}", DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createQuestionWhenTitleIsNullShouldReturn400() throws Exception {
        Question question = questionRepository.findOne(1L);
        question.setTitle(null);
        assertThat(createQuestion(question).getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void createQuestionWhenCourseDoesNotExistsShouldReturn404() throws Exception {
        Question question = questionRepository.findOne(1L);
        question.setCourse(new Course());
        assertThat(createQuestion(question).getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createQuestionWhenEverythingIsRightShouldReturn200() throws Exception {
        Question question = questionRepository.findOne(1L);
        question.setId(null);
        assertThat(createQuestion(question).getStatusCodeValue()).isEqualTo(200);
    }

    private ResponseEntity<String> createQuestion(Question question) {
        BDDMockito.when(questionRepository.save(question)).thenReturn(question);
        return testRestTemplate.exchange("/v1/professor/course/question/", POST,
                new HttpEntity<>(question, professorHeader.getHeaders()), String.class);
    }
}