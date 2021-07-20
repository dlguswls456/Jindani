package org.techtown.Jindani;

import com.google.gson.annotations.SerializedName;

//지금 사용안하고있음
public class ForLevel2 {
    @SerializedName("Sex")
    private String Sex;
    @SerializedName("Age")
    private String Age;
    @SerializedName("Height")
    private String Height;
    @SerializedName("Weight")
    private String Weight;
    @SerializedName("CC")
    private String CC;
    @SerializedName("Location")
    private String Location;
    @SerializedName("Onset")
    private String Onset;

    @SerializedName("First")
    private String First;

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getOnset() {
        return Onset;
    }

    public void setOnset(String onset) {
        Onset = onset;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }
}
