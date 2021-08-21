package com.chomedicine.jindani.network;

import com.chomedicine.jindani.models.Disease;
import com.chomedicine.jindani.models.DiseaseInfo;
import com.chomedicine.jindani.models.Level2;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
