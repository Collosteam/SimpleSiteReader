package com.collosteam.simplesitereader.api;

import retrofit.RestAdapter;
import retrofit.http.GET;

import static com.collosteam.simplesitereader.api.Utils.LessonListWraper;

/**
 * Created by Collos on 8/6/2014.
 */
public class NetworkManager {
    WebAcademyService service;

    public NetworkManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.collosteam.com")
                .build();
        service = restAdapter.create(WebAcademyService.class);
    }

    public LessonListWraper getAllLess(){
        return service.getAllLessons();
    }

    public interface WebAcademyService {
      @GET("/get_all_lessons.php")
      public  LessonListWraper getAllLessons();
    }

}
