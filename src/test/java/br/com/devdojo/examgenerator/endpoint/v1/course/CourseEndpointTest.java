package br.com.devdojo.examgenerator.endpoint.v1.course;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.respository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.respository.ProfessorRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author William Suane for DevDojo on 11/3/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseEndpointTest {
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private ProfessorRepository professorRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Course course = mockCourse();

    private static Course mockCourse() {
        return Course.Builder.newCourse()
                .id(1L)
                .name("Java")
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
        headers.add("Authorization","1111");
        this.professorHeader = new HttpEntity<>(headers);
    }
    @Before
    public void setup(){
        BDDMockito.when(courseRepository.findOne(course.getId())).thenReturn(course);
        BDDMockito.when(courseRepository.listCourses("")).thenReturn(Collections.singletonList(course));
        BDDMockito.when(courseRepository.listCourses("java")).thenReturn(Collections.singletonList(course));
    }
}