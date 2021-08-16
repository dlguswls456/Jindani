package org.techtown.Jindani.models;

public class DoctorAccount {
    private String idToken;
    private String emailId;
    private String name;
    private String licenseNumber;
    private String dept;

    public DoctorAccount() {
    }

    public DoctorAccount(String idToken, String emailId, String name, String licenseNumber, String dept) {
        this.idToken = idToken;
        this.emailId = emailId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.dept = dept;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
