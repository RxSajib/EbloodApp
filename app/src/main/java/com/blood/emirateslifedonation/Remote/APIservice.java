package com.blood.emirateslifedonation.Remote;


import com.blood.emirateslifedonation.Model.Myresponce;
import com.blood.emirateslifedonation.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by HASIB on 12/16/2017.
 */

public interface APIservice {
    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAaIh9QMU:APA91bF4CaQl93whSlLUVuGfAgfU_zjamPA0k1RAdgeYkDavUVuI5IvzjMlJkwVYAMWGtO1HUnLj-XiRPhef-93ZFVWHThCvQF3W2SXzm0tq5se6NxaXT2iXtoryK43LTZjOP9uFhBRT"
            }
    )
    @POST("fcm/send")
    Call<Myresponce> sendNotification(@Body Sender body);



    //Call<Myresponce>

}