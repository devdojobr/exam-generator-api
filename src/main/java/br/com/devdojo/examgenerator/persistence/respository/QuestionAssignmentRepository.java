package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Question;

/**
 * @author William Suane for DevDojo on 12/15/17.
 */
@SuppressWarnings("ALL")
public interface QuestionAssignmentRepository extends CustomPagingAndSortRepository<Question, Long> {

}
