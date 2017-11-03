package br.com.devdojo.examgenerator.util;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import br.com.devdojo.examgenerator.persistence.model.Professor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@Service
public class EndpointUtil implements Serializable {
    public ResponseEntity<?> returnObjectOrNotFound(Object object) {
        return object == null ? new ResponseEntity<>(NOT_FOUND) : new ResponseEntity<>(object, HttpStatus.OK);
    }
    public ResponseEntity<?> returnObjectOrNotFound(List<?> list) {
        return list == null || list.isEmpty() ? new ResponseEntity<>(NOT_FOUND) : new ResponseEntity<>(list, HttpStatus.OK);
    }
    public Professor extractProfessorFromToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((ApplicationUser) authentication.getPrincipal()).getProfessor();
    }
}
