package br.com.devdojo.examgenerator.persistence.respository;

import br.com.devdojo.examgenerator.persistence.model.Professor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author William Suane for DevDojo on 10/24/17.
 */
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {
    Professor findByEmail(String email);
}
