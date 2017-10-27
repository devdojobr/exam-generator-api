package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

    Course findByIdAndProfessor(long id, Professor professor);

}
