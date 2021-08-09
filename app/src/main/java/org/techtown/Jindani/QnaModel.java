package org.techtown.Jindani;

public class QnaModel {

    String userid; //사용자 아이디
    String qid; //질문 아이디
    String q_title; //질문 제목
    String q_content; //질문 내용
    String q_date; //질문 작성 시점

    public QnaModel() { } // DataSnapshot.getValue(QnaModel.class) 위한 생성자

    public QnaModel(String userid, String qid, String q_title, String q_content, String q_date) {
        this.userid = userid;
        this.qid = qid;
        this.q_title = q_title;
        this.q_content = q_content;
        this.q_date = q_date;
    }

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getQid() {
        return qid;
    }
    public void setQid(String qid) {
        this.qid = qid;
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

    public String getQ_date() {
        return q_date;
    }
    public void setQ_date(String q_date) {
        this.q_date = q_date;
    }
}
