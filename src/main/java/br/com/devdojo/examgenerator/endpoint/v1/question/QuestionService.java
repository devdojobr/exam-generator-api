package br.com.devdojo.examgenerator.endpoint.v1.question;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.respository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author William Suane for DevDojo on 10/27/17.
 */
@Service
public class QuestionService implements Serializable {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void throwResourceNotFoundIfQuestionDoesNotExist(Question question){
        if(question == null || question.getId() == null || questionRepository.findOne(question.getId()) == null)
            throw new ResourceNotFoundException("Question not found");
    }
    public void throwResourceNotFoundIfQuestionDoesNotExist(long questionId){
        if(questionId == 0 || questionRepository.findOne(questionId) == null)
            throw new ResourceNotFoundException("Question not found");
    }
}
