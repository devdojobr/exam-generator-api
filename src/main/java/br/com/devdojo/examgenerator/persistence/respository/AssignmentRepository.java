package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Assignment;
import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author William Suane for DevDojo on 12/07/17.
 */
@SuppressWarnings("ALL")
public interface AssignmentRepository extends CustomPagingAndSortRepository<Assignment, Long> {
    @Query("select a from Assignment a where a.course.id = ?1 and a.title like %?2% and a.professor = ?#{principal.professor} and a.enabled = true")
    List<Question> listAssignemntsByCourseAndTitle(long courseId, String title);

    @Query("update Assignment a set a.enabled = false where a.course.id = ?1 and a.professor = ?#{principal.professor} and a.enabled = true")
    @Modifying
    void deleteAllAssignmentsRelatedToCourse(long courseId);
}