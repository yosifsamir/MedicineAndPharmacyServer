package com.example.mypharmapp.api;



import com.example.mypharmapp.notification.MyResponse;
import com.example.mypharmapp.notification.Sender;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAQNYSpQM:APA91bF75WqBsbyIq-86HmmqpyET89bTQ44xtyGT69TE5AavPRQvlM5cUTl0-bLOSYsRoFLO8BX4t8URzd6_5eLj7BlcTfSZE5HyGq2KpKVd9_-FInkPi7AgCh8EZfGI-_-bDYcQWJSH"
            }
    )

    @POST("fcm/send")
    Observable<MyResponse> sendNotification(@Body Sender body);
}