package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Entity;

/**
 * @author William Suane on 29/08/2018
 */
@Entity
public class ExamAnswer extends AbstractEntity {
    private Long questionId;
    private Long assignmentId;
    private Long questionAssignmentId;
    private Long professorId;
    private Long studentId;
    private String assignmentTitle;
    private String questionTitle;
    private Long selectedChoiceId;
    private Long correctChoiceId;
    private String selectedChoiceTitle;
    private String correctChoiceTitle;
    private double choiceGrade;
    private double answerGrade;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getQuestionAssignmentId() {
        return questionAssignmentId;
    }

    public void setQuestionAssignmentId(Long questionAssignmentId) {
        this.questionAssignmentId = questionAssignmentId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public Long getSelectedChoiceId() {
        return selectedChoiceId;
    }

    public void setSelectedChoiceId(Long selectedChoiceId) {
        this.selectedChoiceId = selectedChoiceId;
    }

    public Long getCorrectChoiceId() {
        return correctChoiceId;
    }

    public void setCorrectChoiceId(Long correctChoiceId) {
        this.correctChoiceId = correctChoiceId;
    }

    public String getSelectedChoiceTitle() {
        return selectedChoiceTitle;
    }

    public void setSelectedChoiceTitle(String selectedChoiceTitle) {
        this.selectedChoiceTitle = selectedChoiceTitle;
    }

    public String getCorrectChoiceTitle() {
        return correctChoiceTitle;
    }

    public void setCorrectChoiceTitle(String correctChoiceTitle) {
        this.correctChoiceTitle = correctChoiceTitle;
    }

    public double getChoiceGrade() {
        return choiceGrade;
    }

    public void setChoiceGrade(double choiceGrade) {
        this.choiceGrade = choiceGrade;
    }

    public double getAnswerGrade() {
        return answerGrade;
    }

    public void setAnswerGrade(double answerGrade) {
        this.answerGrade = answerGrade;
    }

    public static final class ExamAnswerBuilder {
        protected Long id;
        private Long questionId;
        private Long assignmentId;
        private Long questionAssignmentId;
        private Long professorId;
        private Long studentId;
        private String assignmentTitle;
        private String questionTitle;
        private boolean enabled = true;
        private Long selectedChoiceId;
        private Long correctChoiceId;
        private String selectedChoiceTitle;
        private String correctChoiceTitle;
        private double choiceGrade;
        private double answerGrade;

        private ExamAnswerBuilder() {
        }

        public static ExamAnswerBuilder newExamAnswer() {
            return new ExamAnswerBuilder();
        }

        public ExamAnswerBuilder questionId(Long questionId) {
            this.questionId = questionId;
            return this;
        }

        public ExamAnswerBuilder assignmentId(Long assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public ExamAnswerBuilder questionAssignmentId(Long questionAssignmentId) {
            this.questionAssignmentId = questionAssignmentId;
            return this;
        }

        public ExamAnswerBuilder professorId(Long professorId) {
            this.professorId = professorId;
            return this;
        }

        public ExamAnswerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ExamAnswerBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public ExamAnswerBuilder assignmentTitle(String assignmentTitle) {
            this.assignmentTitle = assignmentTitle;
            return this;
        }

        public ExamAnswerBuilder questionTitle(String questionTitle) {
            this.questionTitle = questionTitle;
            return this;
        }

        public ExamAnswerBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ExamAnswerBuilder selectedChoiceId(Long selectedChoiceId) {
            this.selectedChoiceId = selectedChoiceId;
            return this;
        }

        public ExamAnswerBuilder correctChoiceId(Long correctChoiceId) {
            this.correctChoiceId = correctChoiceId;
            return this;
        }

        public ExamAnswerBuilder selectedChoiceTitle(String selectedChoiceTitle) {
            this.selectedChoiceTitle = selectedChoiceTitle;
            return this;
        }

        public ExamAnswerBuilder correctChoiceTitle(String correctChoiceTitle) {
            this.correctChoiceTitle = correctChoiceTitle;
            return this;
        }

        public ExamAnswerBuilder choiceGrade(double choiceGrade) {
            this.choiceGrade = choiceGrade;
            return this;
        }

        public ExamAnswerBuilder answerGrade(double answerGrade) {
            this.answerGrade = answerGrade;
            return this;
        }

        public ExamAnswer build() {
            ExamAnswer examAnswer = new ExamAnswer();
            examAnswer.setId(id);
            examAnswer.setEnabled(enabled);
            examAnswer.studentId = this.studentId;
            examAnswer.questionId = this.questionId;
            examAnswer.choiceGrade = this.choiceGrade;
            examAnswer.selectedChoiceId = this.selectedChoiceId;
            examAnswer.answerGrade = this.answerGrade;
            examAnswer.correctChoiceTitle = this.correctChoiceTitle;
            examAnswer.assignmentTitle = this.assignmentTitle;
            examAnswer.selectedChoiceTitle = this.selectedChoiceTitle;
            examAnswer.professorId = this.professorId;
            examAnswer.questionTitle = this.questionTitle;
            examAnswer.correctChoiceId = this.correctChoiceId;
            examAnswer.assignmentId = this.assignmentId;
            examAnswer.questionAssignmentId = this.questionAssignmentId;
            return examAnswer;
        }
    }
}
