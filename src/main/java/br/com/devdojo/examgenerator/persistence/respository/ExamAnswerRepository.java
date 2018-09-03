package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.ExamAnswer;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author William Suane on 29/08/2018
 */
public interface ExamAnswerRepository extends PagingAndSortingRepository<ExamAnswer, Long> {
    boolean existsExamAnswerByAssignmentIdAndStudentId(long assignmentId, long studentId);
}
