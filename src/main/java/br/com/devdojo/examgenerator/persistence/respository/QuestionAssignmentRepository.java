package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.QuestionAssignment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author William Suane for DevDojo on 12/15/17.
 */
@SuppressWarnings("ALL")
public interface QuestionAssignmentRepository extends CustomPagingAndSortRepository<QuestionAssignment, Long> {
    @Query("update QuestionAssignment qa set qa.enabled = false where qa.assignment.id in (select a.id from Assignment a where a.course.id = ?1) and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToCourse(long courseId);

    @Query("update QuestionAssignment qa set qa.enabled = false where qa.assignment.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToAssignment(long assignmentId);

    @Query("update QuestionAssignment qa set qa.enabled = false where qa.question.id = ?1 and qa.professor = ?#{principal.professor} and qa.enabled = true")
    @Modifying
    void deleteAllQuestionAssignmentsRelatedToQuestion(long questionId);
}
