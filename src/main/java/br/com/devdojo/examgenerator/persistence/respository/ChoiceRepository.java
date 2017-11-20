package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author William Suane for DevDojo on 11/20/17.
 */
public interface ChoiceRepository extends CustomPagingAndSortRepository<Choice, Long> {
    @Query("select c from Choice c where c.question.id = ?1 and q.professor = ?#{principal.professor} and q.enabled = true")
    List<Choice> listChoicesByQuestionId(long questionId);

    @Query("update Choice c set c.correctAnswer = false where c <> ?1 and c.question = ?2 and q.professor = ?#{principal.professor} and q.enabled = true")
    void updateAllOtherChoicesCorrectAnswerToFalse(Choice choice, Question question);
}
