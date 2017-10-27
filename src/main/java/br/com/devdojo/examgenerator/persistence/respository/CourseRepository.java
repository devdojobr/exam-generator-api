package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    @Query("select c from Course c where c.id = ?1 and c.professor = ?#{principal.professor}")
    @Override
    Course findOne(Long aLong);
}
