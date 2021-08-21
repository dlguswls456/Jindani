package com.chomedicine.jindani.models;

//의사 답변에 사용되는 클래스
public class AnswerModel {

    String docId; //의사 아이디
    String docName; //의사 이름
    String docDept; //의사 진료과
    String answerId; //답변 아이디
    String answer_content; //답변 내용
    String answer_date; //답변 작성 시점
    String questionId; //답변한 질문 아이디

    public AnswerModel() { } // DataSnapshot.getValue(QnaModel.class) 위한 생성자

    public AnswerModel(String docId, String docName, String docDept, String answerId, String answer, String answer_date, String questionId) {
        this.docId = docId;
        this.docName = docName;
        this.docDept = docDept;
        this.answerId = answerId;
        this.answer_content = answer;
        this.answer_date = answer_date;
        this.questionId = questionId;
    }

    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }
    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocDept() {
        return docDept;
    }
    public void setDocDept(String docDept) {
        this.docDept = docDept;
    }

    public String getAnswerId() {
        return answerId;
    }
    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer_content() {
        return answer_content;
    }
    public void setAnswer_content(String answer) {
        this.answer_content = answer;
    }

    public String getAnswer_date() {
        return answer_date;
    }
    public void setAnswer_date(String answer_date) {
        this.answer_date = answer_date;
    }

    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
