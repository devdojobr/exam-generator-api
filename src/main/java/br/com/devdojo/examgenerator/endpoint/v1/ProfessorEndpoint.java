package br.com.devdojo.examgenerator.endpoint.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.respository.ProfessorRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author William Suane for DevDojo on 10/10/17.
 */
@RestController
@RequestMapping("v1/professor")
public class ProfessorEndpoint {
    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorEndpoint(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @GetMapping(path = "{id}")
    @ApiOperation(value = "Find professor by his ID", notes = "We have to make this method better", response = Professor.class)
    public ResponseEntity<?> getProfessorById(@PathVariable long id) {
        Professor professor = professorRepository.findOne(id);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }
}
