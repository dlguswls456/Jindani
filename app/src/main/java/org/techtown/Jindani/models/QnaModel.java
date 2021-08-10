package org.techtown.Jindani.models;

public class QnaModel {

    String userId; //사용자 아이디
    String questionId; //질문 아이디
    String question_title; //질문 제목
    String question_content; //질문 내용
    String question_date; //질문 작성 시점

    public QnaModel() { } // DataSnapshot.getValue(QnaModel.class) 위한 생성자

    public QnaModel(String userId, String questionId, String question_title, String question_content, String question_date) {
        this.userId = userId;
        this.questionId = questionId;
        this.question_title = question_title;
        this.question_content = question_content;
        this.question_date = question_date;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion_title() {
        return question_title;
    }
    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestion_content() {
        return question_content;
    }
    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_date() {
        return question_date;
    }
    public void setQuestion_date(String question_date) {
        this.question_date = question_date;
    }
}
