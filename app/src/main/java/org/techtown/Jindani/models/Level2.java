package org.techtown.Jindani.models;

//Level2 예측 후 서버에서 받아올 때 사용되는 클래스
public class Level2 {
    String level2_name;//가장 확률 높은 level2
    String percentage;//두번쨰 level2
    String eng_name;

    public String getLevel2_name() {
        return level2_name;
    }

    public void setLevel2_name(String level2_name) {
        this.level2_name = level2_name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getEng_name() {
        return eng_name;
    }

    public void setEng_name(String eng_name) {
        this.eng_name = eng_name;
    }
}
