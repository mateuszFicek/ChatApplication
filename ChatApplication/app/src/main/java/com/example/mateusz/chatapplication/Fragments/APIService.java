package com.example.mateusz.chatapplication.Fragments;

import com.example.mateusz.chatapplication.Notification.Response;
import com.example.mateusz.chatapplication.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Mateusz on 03.02.2019.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAyoZIFLI:APA91bFr3rLeD89hU8JnmbFaGCKvk38FBEOhsNVE-vO9I1jz4KdeDmMO9ry9x4Lx5_w8Wwj6wHQiB8t_9y1eVe64Nvj5y6FCaJqm3f73mXzY-h0xYcrA7aSKWYeSeJT0_xqHe5h1t8OX"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
