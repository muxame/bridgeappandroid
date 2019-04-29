package net.bridgeint.app.interfaces;

import net.bridgeint.app.models.PaymentHistoryResponse;
import net.bridgeint.app.models.UpdateResponse;
import net.bridgeint.app.models.checkoutResponse.CheckOutResponse;
import net.bridgeint.app.models.getPaymentStatus.GetPaymentResponse;
import net.bridgeint.app.models.successResponse.SavePaymentResponse;
import net.bridgeint.app.models.tokenResponse.TokenLiveResponse;
import net.bridgeint.app.responces.ApplicationResponce;
import net.bridgeint.app.responces.BannerResponce;
import net.bridgeint.app.responces.ChatResponce;
import net.bridgeint.app.responces.CountryResponce;
import net.bridgeint.app.responces.CourseResponce;
import net.bridgeint.app.responces.DegreeResponce;
import net.bridgeint.app.responces.FeesResponse;
import net.bridgeint.app.responces.GenericResponce;
import net.bridgeint.app.responces.RegisterResponse;
import net.bridgeint.app.responces.SuggestKeyowrdResponse;
import net.bridgeint.app.responces.UniversitiesResponce;
import net.bridgeint.app.responces.UploadFileResponce;
import net.bridgeint.app.responces.packages.FreePackageHistory;
import net.bridgeint.app.responces.packages.PackagesResponse;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ufraj on 11/15/2016.
 */
public interface ApiInterface {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.retrofit_base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build();

    //post request for registration
    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> registration(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("login")
    Call<RegisterResponse> login(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("sendCode")
    Call<GenericResponce> sendCode(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("verifyCode")
    Call<GenericResponce> verifyCode(@FieldMap HashMap<String, String> authData);

    @GET("getFeaturedUniversities")
    Call<UniversitiesResponce> getFeatureUniversities(@Query(Constants.USER_ID) String userId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);

    @GET("search")
    Call<UniversitiesResponce> searchUniversities(@Query(Constants.USER_ID) String userId,
                                                  @Query(Constants.ACCESS_KEY) String accessKey,
                                                  @Query(Constants.API_KEY) String apiKey,
                                                  @Query(Constants.KEYWORD) String keyword,
                                                  @Query(Constants.LANGUAGE) String language,
                                                @Query(Constants.CURRENT_PAGE) String currentPage);


    @GET("search")
    Call<UniversitiesResponce> getAllUniversities(@Query(Constants.USER_ID) String userId,
                                                  @Query(Constants.ACCESS_KEY) String accessKey,
                                                  @Query(Constants.API_KEY) String apiKey,
                                                  @Query(Constants.LANGUAGE) String language,
                                                  @Query(Constants.CURRENT_PAGE) String currentPage);


    @GET("autosuggest")
    Call<SuggestKeyowrdResponse> getSearchKeywod(@Query(Constants.KEYWORD) String keyword,
                                                 @Query(Constants.ACCESS_KEY) String accessKey,
                                                 @Query(Constants.API_KEY) String apiKey,
                                                 @Query(Constants.LANGUAGE) String language,
                                                 @Query(Constants.CURRENT_PAGE) String currentPage);


    @GET("getDegrees")
    Call<DegreeResponce> getDegrees(@Query(Constants.USER_ID) String userId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);
    @GET("getTuitionfeesoption")
    Call<FeesResponse> getTuitionfeesoption(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreeId/*, @Query(Constants.STREM_ID) String courseId*/, @Query(Constants.COUNTRY_ID) String countryId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);

    @GET("getCourses")
    Call<CourseResponce> getCourse(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey, @Query(Constants.NEXT_PAGE) String nextPage, @Query(Constants.KEYWORD) String keyword);

    @GET("getStream")
    Call<CourseResponce> getNewCourse(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreId,@Query(Constants.COURSE_TYPE) String courseType, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey, @Query(Constants.NEXT_PAGE) String nextPage, @Query(Constants.KEYWORD) String keyword);
    @GET("getUniversitiesdetails")
    Call<CourseResponce> getCourseForUniDetails(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreId,@Query(Constants.COURSE_TYPE) String courseType, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey, @Query(Constants.NEXT_PAGE) String nextPage, @Query(Constants.KEYWORD) String keyword/*,@Query(Constants.STREM_ID)String stremId*/,@Query(Constants.ID) String id);

    @GET("getCountries")
    Call<CountryResponce> getCountries(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreeId/*, @Query(Constants.STREM_ID) String courseId*/, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);

    // Get University by Degree , Course & country
    @GET("getUniversities")
    Call<UniversitiesResponce> getUniversity(@Query(Constants.USER_ID) String userId, @Query(Constants.DEGREE_ID) String degreeId/*, @Query(Constants.STREM_ID) String courseId*/, @Query(Constants.COUNTRY_ID) String countryId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey,@Query(Constants.FEESOPTIONS) String feeOption);

    @FormUrlEncoded
    @POST("updateProfile")
    Call<UpdateResponse> updateProfile(@FieldMap HashMap<String, String> authData);

    @GET("logout")
    Call<GenericResponce> logout(@Query(Constants.USER_ID) String userId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);

    @FormUrlEncoded
    @POST("contactus")
    Call<GenericResponce> contactUs(@Field(Constants.USER_ID) String userId, @Field(Constants.ACCESS_KEY) String accessKey, @Field(Constants.API_KEY) String apiKey,@Field(Constants.PARAM_USER_NAME)String userName,@Field(Constants.EMAIL)String email,@Field(Constants.MESSAGE)String Message);

    @Multipart
    @POST("imageUpload")
    Call<UploadFileResponce> uploadImage(@Part(Constants.USER_ID) RequestBody userId, @Part(Constants.ACCESS_KEY) RequestBody accessKey, @Part(Constants.API_KEY) RequestBody api_key, @Part MultipartBody.Part image);

    @Multipart
    @POST("updateApplicationsdocs")
    Call<UploadFileResponce> uploadEditImage(@Part(Constants.USER_ID) RequestBody userId, @Part(Constants.ACCESS_KEY) RequestBody accessKey, @Part(Constants.API_KEY) RequestBody api_key, @Part(Constants.TYPE) RequestBody type, @Part(Constants.APPLY_ID) RequestBody applyId, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("applyUniversity")
    Call<GenericResponce> ApplyForApplication(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("updateapplyUniversity")
    Call<GenericResponce> UpdateApplyForApplication(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("checkapply")
    Call<GenericResponce> CheckApplyFor(@Field(Constants.API_KEY) String apiKey, @Field(Constants.USER_ID) String userId, @Field(Constants.ACCESS_KEY) String accessKey, @Field(Constants.UNIVERSITY_ID) String uniId, @Field(Constants.DEGREE_ID) String degreeId, @Field(Constants.COURSE_ID) String corseId, @Field(Constants.COUNTRY_ID) String countryId);

    @GET("myApplications")
    Call<ApplicationResponce> GetMyApplications(@Query(Constants.USER_ID) String userId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey);

    @GET("getLatestMessages")
    Call<ChatResponce> fetchPreviousChat(@Query(Constants.USER_ID) String userId, @Query(Constants.ACCESS_KEY) String accessKey, @Query(Constants.API_KEY) String apiKey, @Query(Constants.CURRENT_PAGE) String currentPage);

    @FormUrlEncoded
    @POST("updateDeviceToken")
    Call<GenericResponce> updateDeviceToken(@FieldMap HashMap<String, String> authData);

    @FormUrlEncoded
    @POST("updateUserlanguage")
    Call<GenericResponce> updateUserlanguage(@FieldMap HashMap<String, String> authData);

    @GET("forgotPassword")
    Call<GenericResponce> ForgetPassword(@Query(Constants.EMAIL) String email,
                                         @Query(Constants.LANGUAGE) String language);

    @GET("getBanners")
    Call<BannerResponce> getBanner();

    @GET("getPackages")
    Call<PackagesResponse> getPackages(@Query(Constants.API_KEY) String apiKey,
                                       @Query(Constants.LANGUAGE) String language);



    @FormUrlEncoded
    @POST("checkoutRequest")
    Call<CheckOutResponse> getCheckOutId(@Field("userId") String userId, @Field("amount") String price, @Field("shopperResultUrl") String shopperUrl);

    @FormUrlEncoded
    @POST("removeapplication")
    Call<GenericResponce> removeAppliedUniversity(@Field("userId") String userId, @Field("accessKey") String accessKey, @Field("applyId") String applyId, @Field("API_KEY") String apply_key);

    @FormUrlEncoded
    @POST("callback")
    Call<GetPaymentResponse> getPaymentStatus(@Field("checkoutId") String checkID, @Field("userId") String user_id);

    @FormUrlEncoded
    @POST("getLiveSessionToken")
    Call<TokenLiveResponse> getToken(@Field("accessKey") String accessKey);


    @FormUrlEncoded
    @POST("saveSuccessPayments")
    Call<SavePaymentResponse> savePayment(@Field("checkoutId") String checkID, @Field("userId") String userId, @Field("response") String response, @Field("packageId") String packageId, @Field("amount") String amount);


    @GET("getpaymentservicehistory")
    Call<PaymentHistoryResponse> getPaymentHistory(@Query(Constants.USER_ID) String userId,
                                                   @Query(Constants.ACCESS_KEY) String accessKey,
                                                   @Query(Constants.API_KEY) String apiKey,
                                                   @Query(Constants.LANGUAGE) String language);

    @FormUrlEncoded
    @POST("getfreepackagehistory")
    Call<FreePackageHistory> getFreeHistory(@Field(Constants.USER_ID) String userId,
                                            @Field(Constants.ACCESS_KEY) String accessKey,
                                            @Field(Constants.API_KEY) String apiKey,
                                            @Field(Constants.LANGUAGE) String language);

    /*===========================================*/
    @FormUrlEncoded
    @POST("changenotificationstatus")
    Call<UpdateResponse> changeNotificationStatus(@FieldMap HashMap<String, String> authData);
    /*===========================================*/

    String hyper_pay_url = "https://test.oppwa.com/v1/";

    Retrofit retrofit_hyperPay = new Retrofit.Builder()
            .baseUrl(hyper_pay_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build();

}
