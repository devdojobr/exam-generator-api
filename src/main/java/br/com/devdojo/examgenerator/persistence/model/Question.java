package br.com.devdojo.examgenerator.persistence.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author William Suane for DevDojo on 11/10/17.
 */
@Entity
public class Question extends AbstractEntity {
    @NotEmpty(message = "The field title cannot be empty")
    @ApiModelProperty(notes = "The title of the question")
    private String title;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Professor professor;


    public static final class QuestionBuilder {
        private Question question;

        private QuestionBuilder() {
            question = new Question();
        }

        public static QuestionBuilder newQuestion() {
            return new QuestionBuilder();
        }

        public QuestionBuilder id(Long id) {
            question.setId(id);
            return this;
        }

        public QuestionBuilder enabled(boolean enabled) {
            question.setEnabled(enabled);
            return this;
        }

        public QuestionBuilder title(String title) {
            question.setTitle(title);
            return this;
        }

        public QuestionBuilder course(Course course) {
            question.setCourse(course);
            return this;
        }

        public QuestionBuilder professor(Professor professor) {
            question.setProfessor(professor);
            return this;
        }

        public Question build() {
            return question;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
