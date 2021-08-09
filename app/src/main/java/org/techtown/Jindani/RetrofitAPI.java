package org.techtown.Jindani;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    //level2받아올 때 사용
    @POST("/level2")
    Call<List<Level2>> postAnswer_for_Level2(@Body HashMap<String, String> param);

    //질병 받아올 때 사용
    @POST("/disease")
    Call<List<Disease>> postAnswer_for_Disease(@Body HashMap<String, String> param);

    //질병 정보 받아올 때 사용
    @POST("/disease_info")
    Call<List<DiseaseInfo>> postDisease_for_Disease_info(@Body HashMap<String, String> param);

}
