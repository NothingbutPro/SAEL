package dev.raghav.sael.ModelClass;

public class QuestionListModel {

    private String q_id;
    private String question;
    private String answer;
    private String level;
    private String sr_no;


    public QuestionListModel(String q_id, String question, String answer, String level, String sr_no) {
    this.q_id=q_id;
    this.question=question;
    this.answer=answer;
    this.level=level;
    this.sr_no=sr_no;


    }

    public String getAnswer() {
        return answer;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQ_id() {
        return q_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }
}
