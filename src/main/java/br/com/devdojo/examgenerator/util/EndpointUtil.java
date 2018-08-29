package br.com.devdojo.examgenerator.util;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@Service
public class EndpointUtil implements Serializable {
    public ResponseEntity<?> returnObjectOrNotFound(Object object) {
        if (object == null) throw new ResourceNotFoundException("Not found");
        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    public ResponseEntity<?> returnObjectOrNotFound(List<?> list) {
        if (list == null || list.isEmpty()) throw new ResourceNotFoundException("Not found");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public Professor extractProfessorFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((ApplicationUser) authentication.getPrincipal()).getProfessor();
    }

    public Student extractStudentFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((ApplicationUser) authentication.getPrincipal()).getStudent();
    }
}
