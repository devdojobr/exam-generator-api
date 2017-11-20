package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@SuppressWarnings("ALL")
public interface QuestionRepository extends CustomPagingAndSortRepository<Question, Long> {
    @Query("select q from Question q where q.course.id = ?1 and q.title like %?2% and q.professor = ?#{principal.professor} and q.enabled = true")
    List<Question> listQuestionsByCourseAndTitle(long courseId, String title);

    @Query("update Question q set q.enabled = false where q.course.id = ?1 and q.professor = ?#{principal.professor} and q.enabled = true")
    @Modifying
    void deleteAllQuestionsRelatedToCourse(long courseId);
}