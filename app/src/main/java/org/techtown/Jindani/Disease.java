package org.techtown.Jindani;

//질병 예측 후 서버에서 받아올 때 사용되는 클래스
public class Disease {
    String disease_name;//예측된 병
    String percentage;//확률

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
