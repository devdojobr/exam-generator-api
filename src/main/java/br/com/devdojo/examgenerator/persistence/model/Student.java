package br.com.devdojo.examgenerator.persistence.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author William Suane on 07/04/2018
 */
@Entity
public class Student extends AbstractEntity {
    @NotEmpty(message = "The field name cannot be empty")
    private String name;
    @Email(message = "This email is not valid")
    @NotEmpty(message = "The field email cannot be empty")
    @Column(unique = true)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final class Builder {
        private Student student;

        private Builder() {
            student = new Student();
        }

        public static Builder newStudent() {
            return new Builder();
        }

        public Builder id(Long id) {
            student.setId(id);
            return this;
        }

        public Builder name(String name) {
            student.setName(name);
            return this;
        }

        public Builder enabled(boolean enabled) {
            student.setEnabled(enabled);
            return this;
        }

        public Builder email(String email) {
            student.setEmail(email);
            return this;
        }

        public Student build() {
            return student;
        }
    }
}
