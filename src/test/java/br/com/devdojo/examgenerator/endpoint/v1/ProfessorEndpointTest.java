package br.com.devdojo.examgenerator.endpoint.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;

import static org.junit.Assert.*;

/**
 * @author William Suane for DevDojo on 11/3/17.
 */
public class ProfessorEndpointTest {
    public static Professor mockProfessor(){
        return Professor.Builder.newProfessor()
                .id(1L)
                .name("Will")
                .email("will@something.com")
                .build();
    }
}