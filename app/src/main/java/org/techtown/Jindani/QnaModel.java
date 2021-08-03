package org.techtown.Jindani;

public class QnaModel {

    String qora; //질문 or 답변 내용
    boolean isDoc; //발화자 구분

    public QnaModel(String qora, boolean isDoc) {
        this.qora = qora;
        this.isDoc = isDoc;
    }

    public String getQora() {
        return qora;
    }

    public void setQora(String qora) {
        this.qora = qora;
    }

    public boolean isDoc() {
        return isDoc;
    }

    public void setDoc(boolean doc) {
        isDoc = doc;
    }
}
