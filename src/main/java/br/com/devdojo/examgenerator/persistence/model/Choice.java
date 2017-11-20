package br.com.devdojo.examgenerator.persistence.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author William Suane for DevDojo on 11/20/17.
 */
@Entity
public class Choice extends AbstractEntity{
    @NotEmpty(message = "The field title cannot be empty")
    @ApiModelProperty(notes = "The title of the choice")
    private String title;
    @NotEmpty(message = "The field correctAnswer must be true or false")
    @ApiModelProperty(notes = "Correct answer for the associated question, you can have only one correct answer per question")
    private boolean correctAnswer;
    @ManyToOne(optional = false)
    private Question question;
    @ManyToOne(optional = false)
    private Professor professor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
