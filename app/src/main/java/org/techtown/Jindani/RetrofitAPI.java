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
//    @GET("/posts")
//    Call<List<Post>> getData(@Query("userId") String id);
//
//    @FormUrlEncoded
//    @POST("/posts")
//    Call<Post> postData(@FieldMap HashMap<String, Object> param);

    //level2받아올 때 사용
    @POST("/level2")
    Call<Level2> postAnswer_for_Level2(@Body HashMap<String, String> param);

    //질병 받아올 때 사용
    @POST("/disease")
    Call<List<Disease>> postAnswer_for_Disease(@Body HashMap<String, String> param);

}
