package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@SuppressWarnings("ALL")
public interface CourseRepository extends CustomPagingAndSortRepository<Course, Long> {
    @Query("select c from Course c where c.name like %?1% and c.professor = ?#{principal.professor} and c.enabled = true")
    List<Course> listCoursesByName(String name);
}