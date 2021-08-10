package org.techtown.Jindani.models;

//사용자 정보 클래스
public class UserAccount {
    private String idToken;
    private String emailId;
    private String password;
    private String sex;
    private String birthDate;
    private String height;
    private String weight;
    private String past;
    private String social;
    private String family;

    public UserAccount() {}

    public UserAccount(String idToken, String emailId, String password, String sex, String birthDate, String height, String weight, String past, String social, String family) {
        this.idToken = idToken;
        this.emailId = emailId;
        this.password = password;
        this.sex = sex;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.past = past;
        this.social = social;
        this.family = family;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String age) {
        this.birthDate = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPast() {
        return past;
    }

    public void setPast(String past) {
        this.past = past;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}


