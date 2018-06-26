package com.st18apps.testtask.api;



import com.st18apps.testtask.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("users")
    Call<List<User>> getUsers();

}
