package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@SuppressWarnings("ALL")
public interface QuestionAssignmentRepository extends CustomPagingAndSortRepository<Question, Long> {
    @Query("select qa from QuestionAssignment qa where qa.assignment.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
    List<Question> listQuestionsAssignmentByAssignment(long assigmentId);
}