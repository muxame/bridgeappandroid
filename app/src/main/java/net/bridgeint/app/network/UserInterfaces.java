package net.bridgeint.app.network;

import net.bridgeint.app.responces.packages.ApplyResponse;
import net.bridgeint.app.responces.packages.FreepackagesResponse;
import net.bridgeint.app.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserInterfaces {

    @GET("getPackagesfree")
    Call<FreepackagesResponse> getFreepackage();

    @FormUrlEncoded
    @POST("applyfreepackage")
    Call<ApplyResponse> applyfreepackage(@Field(Constants.USER_ID) String userId,
                                         @Field(Constants.ACCESS_KEY) String accessKey,
                                         @Field(Constants.API_KEY) String apiKey,
                                         @Field(Constants.COUNTRY_ID_FREE) String countryId,
                                         @Field(Constants.COUNTRY_NAME_FREE) String countryName,
                                         @Field(Constants.UNIVERSITIES_ID_FREE) String universityId,
                                         @Field(Constants.UNIVERSITIES_NAME_FREE) String universityName,
                                         @Field(Constants.DOCUMENT_FREE) String doument,
                                         @Field(Constants.DOCUMENT_TYPE_FREE) String doumentType,
                                         @Field(Constants.COMMENT_FREE) String comment
    );


}
