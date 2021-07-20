package org.techtown.Jindani;

//Level2 예측 후 서버에서 받아올 때 사용되는 클래스
public class Level2 {
    String First;//가장 확률 높은 level2
    String Second;//두번쨰 level2

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String second) {
        Second = second;
    }
}
