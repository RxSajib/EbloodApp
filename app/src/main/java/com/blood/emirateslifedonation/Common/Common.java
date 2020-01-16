package com.blood.emirateslifedonation.Common;


import com.blood.emirateslifedonation.Remote.APIservice;
import com.blood.emirateslifedonation.Remote.FCMretrofitClient;

public class Common {


    public static final String BaseUrl="https://fcm.googleapis.com/";




    public static APIservice getFCMClient(){
        return FCMretrofitClient.getClint(BaseUrl).create(APIservice.class);
    }

}
