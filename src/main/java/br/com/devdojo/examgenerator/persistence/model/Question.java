package br.com.devdojo.examgenerator.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author William Suane for DevDojo on 11/10/17.
 */
@Entity
public class Question extends AbstractEntity {
    @NotEmpty(message = "The field title cannot be empty")
    @ApiModelProperty(notes = "The title of the question")
    private String title;
    @ManyToOne(optional = false)
    private Course course;
    @ManyToOne(optional = false)
    private Professor professor;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Choice> choices;


    public static final class Builder {
        private Question question;

        private Builder() {
            question = new Question();
        }

        public static Builder newQuestion() {
            return new Builder();
        }

        public Builder id(Long id) {
            question.setId(id);
            return this;
        }

        public Builder enabled(boolean enabled) {
            question.setEnabled(enabled);
            return this;
        }

        public Builder title(String title) {
            question.setTitle(title);
            return this;
        }

        public Builder course(Course course) {
            question.setCourse(course);
            return this;
        }

        public Builder professor(Professor professor) {
            question.setProfessor(professor);
            return this;
        }

        public Question build() {
            return question;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
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

