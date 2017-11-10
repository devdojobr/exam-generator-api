package br.com.devdojo.examgenerator.endpoint.v1.genericservice;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.AbstractEntity;
import br.com.devdojo.examgenerator.persistence.respository.CustomPagingAndSortRepository;
import org.springframework.stereotype.Service;

/**
 * @author William Suane for DevDojo on 11/10/17.
 */
@Service
public class GenericService {

    public <T extends AbstractEntity, ID extends Long> void throwResourceNotFoundIfDoesNotExist(T t, CustomPagingAndSortRepository<T, ID> repository, String msg) {
        if (t == null || t.getId() == null || repository.findOne(t.getId()) == null)
            throw new ResourceNotFoundException(msg);
    }

    public <T extends AbstractEntity, ID extends Long> void throwResourceNotFoundIfDoesNotExist(long id, CustomPagingAndSortRepository<T, ID> repository, String msg) {
        if (id == 0 || repository.findOne(id) == null)
            throw new ResourceNotFoundException(msg);
    }
}
