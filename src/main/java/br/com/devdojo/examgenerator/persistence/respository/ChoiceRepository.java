package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author William Suane for DevDojo on 11/20/17.
 */
public interface ChoiceRepository extends CustomPagingAndSortRepository<Choice, Long> {
    @Query("select c from Choice c where c.question.id = ?1 and c.professor = ?#{principal.professor} and c.enabled = true")
    List<Choice> listChoicesByQuestionId(long questionId);

    @Query("select c from Choice c where c.question.id in ?1 and c.enabled = true")
    List<Choice> listChoicesByQuestionsIdForStudent(List<Long> questionsId);

    @Query("update Choice c set c.correctAnswer = false where c <> ?1 and c.question = ?2 and c.professor = ?#{principal.professor} and c.enabled = true")
    @Modifying
    void updateAllOtherChoicesCorrectAnswerToFalse(Choice choice, Question question);

    @Query("update Choice c set c.enabled = false where c.question.id = ?1 and c.professor = ?#{principal.professor} and c.enabled = true")
    @Modifying
    void deleteAllChoicesRelatedToQuestion(long questionId);

    @Query("update Choice c set c.enabled = false where c.question.id in (select q.id from Question q where q.course.id = ?1) and c.professor = ?#{principal.professor} and c.enabled = true")
    @Modifying
    void deleteAllChoicesRelatedToCourse(long courseId);

    @Query("select c from Choice c where c.id =?1 and c.enabled = true")
    Choice findOne(Long id);

    @Query("select c from Choice c where c.question.id = ?1 and c.correctAnswer = true and c.enabled = true")
    Choice findCorrectChoiceForQuestion(long questionId);
}
