package org.techtown.Jindani.models;

//질문목록 저장용
public class Questions {
    String tag;//질문 태그(CC, Location, ...)
    String question;//질문 텍스트

    public Questions(String tag, String question) {
        this.tag = tag;
        this.question = question;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
