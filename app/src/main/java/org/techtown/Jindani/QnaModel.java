package org.techtown.Jindani;

public class QnaModel {

    String q_title; //질문 제목
    String q_content; //질문 내용
    String answer; //답변 내용
    boolean isDoc; //발화자 구분

    public QnaModel(String q_title, String q_content, String answer, boolean isDoc) {
        this.q_title = q_title;
        this.q_content = q_content;
        this.answer = answer;
        this.isDoc = isDoc;
    }

    public String getQ_title() {
        return q_title;
    }

    public void setQ_title(String q_title) {
        this.q_title = q_title;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isDoc() {
        return isDoc;
    }

    public void setDoc(boolean doc) {
        isDoc = doc;
    }
}
