package com.google.codeu.chatme.utility.network;

import com.google.codeu.chatme.model.ConversationParticipantDetails;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FirebaseService {

    /**
     * @param participants
     * @return
     */
    @FormUrlEncoded
    @POST("/getUserNames")
    Call<HashMap<String, String>> getNamesFromIds(@Field("ids") List<String> participants);

    @FormUrlEncoded
    @POST("/getUserDetails")
    Call<HashMap<String, ConversationParticipantDetails>> getDetailsFromIds(
            @Field("ids") List<String> participants);

}