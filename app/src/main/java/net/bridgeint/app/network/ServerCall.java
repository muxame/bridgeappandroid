package net.bridgeint.app.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import net.bridgeint.app.R;
import net.bridgeint.app.interfaces.ApiInterface;
import net.bridgeint.app.interfaces.ResponceCallback;
import net.bridgeint.app.models.UpdateResponse;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
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
import net.bridgeint.app.responces.UniversitiesResponce;
import net.bridgeint.app.responces.UploadFileResponce;
import net.bridgeint.app.responces.packages.PackagesResponse;
import net.bridgeint.app.utils.Config;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.SessionManager;
import net.bridgeint.app.utils.SharedClass;
import net.bridgeint.app.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.bridgeint.app.utils.Config.PAYMENT_COMMENT_TEXT;

/**
 * Created by DeviceBee on 8/16/2017.
 */

public class ServerCall {

    public static void AuthenticateUser(final Activity activity, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);

            HashMap<String, String> paramMap = new HashMap<String, String>();

            paramMap.put(Constants.EMAIL, Utility.getUserEmail(activity));
            paramMap.put(Constants.PASSWORD, Utility.getUserPassword(activity));
            paramMap.put(Constants.DEVIC_TYPE, Config.device_type);
            paramMap.put(Constants.LANGUAGE,Constants.LANGUAGE);
            paramMap.put(Constants.DEVICE_TOKEN, Utility.getDeviceToken(activity));

            final Call<RegisterResponse> call_register = api.login(paramMap);
            call_register.enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void signUp(final Activity context, final ProgressDialog dialog, final String firstName, String lastname, final String email, String password, String name, String userType, final String deviceToken, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, context.getString(R.string.please_wait), context);
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.EMAIL, email);
            paramMap.put(Constants.PASSWORD, password);
            paramMap.put(Constants.NAME, name);
            paramMap.put(Constants.FIRST_NAME, firstName);
            paramMap.put(Constants.LAST_NAME, lastname);
            paramMap.put(Constants.TYPE, "Student");
            paramMap.put(Constants.DEVICE_TOKEN, deviceToken);
            paramMap.put(Constants.DEVIC_TYPE, Config.device_type);
            paramMap.put(Constants.LANGUAGE, SessionManager.get(Constants.LANGUAGE));
            final Call<RegisterResponse> call_register = api.registration(paramMap);
            call_register.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                        RegisterResponse register_response = response.body();

                    } else {
                        responceCallback.onFailureResponce(response);
                        Utility.dismissProgressDialog(dialog);

                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }

    public static void signUpFb(String fbId, final Activity context, final ProgressDialog dialog, final String email, String name, String last_name, String userType, final String deviceToken, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, context.getString(R.string.please_wait), context);

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.EMAIL, email);
            paramMap.put(Constants.NAME, name);
            paramMap.put("fbId", fbId);
            paramMap.put(Constants.TYPE, userType);
            paramMap.put(Constants.PASSWORD, fbId);
            paramMap.put(Constants.FIRST_NAME, name);
            paramMap.put(Constants.LAST_NAME, last_name);
            paramMap.put(Constants.DEVICE_TOKEN, deviceToken);
            paramMap.put(Constants.DEVIC_TYPE, Config.device_type);
            paramMap.put(Constants.LANGUAGE, SessionManager.get(Constants.LANGUAGE));

            final Call<RegisterResponse> call_register = api.registration(paramMap);
            call_register.enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                        RegisterResponse register_response = response.body();

                    } else {
                        responceCallback.onFailureResponce(response);
                        Utility.dismissProgressDialog(dialog);

                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    t.printStackTrace();
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }

    public static void sendNumberToGetCode(final Activity activity, final ProgressDialog dialog, String number, final ResponceCallback responceCallback) {

        try {
            Utility.showProgressDialog(dialog, activity.getString(R.string.please_wait), activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
            paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
            paramMap.put(Constants.PHONE, number);
            paramMap.put(Constants.API_KEY, Config.API_KEY);


            final Call<GenericResponce> call_register = api.sendCode(paramMap);
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                        Utility.dismissProgressDialog(dialog);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }

    public static void sendVerificationCode(final Activity activity, final ProgressDialog dialog, String code, final ResponceCallback responceCallback) {
        try {
            Utility.showProgressDialog(dialog, activity.getString(R.string.please_wait), activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
            paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
            paramMap.put(Constants.SMS_CODE, code);
            paramMap.put(Constants.API_KEY, Config.API_KEY);

            final Call<GenericResponce> call_register = api.verifyCode(paramMap);
            call_register.enqueue(new Callback<GenericResponce>() {
                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                        Utility.dismissProgressDialog(dialog);

                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }

    public static void signIn(final Activity context, final ProgressDialog dialog, final String email, final String password, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, context.getString(R.string.please_wait), context);
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.EMAIL, email);
            paramMap.put(Constants.PASSWORD, password);
            paramMap.put(Constants.DEVIC_TYPE, Config.device_type);
            paramMap.put(Constants.LANGUAGE, Constants.LANGUAGE);
            paramMap.put(Constants.DEVICE_TOKEN, Utility.getDeviceToken(context));
            final Call<RegisterResponse> call_register = api.login(paramMap);
            call_register.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                        Utility.dismissProgressDialog(dialog);
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }

    public static void FetchFeaturedUniversities(final Activity context, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            Log.d("AT@Dashboard", accessToken);
            final Call<UniversitiesResponce> call_register = api.getFeatureUniversities(user_id, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<UniversitiesResponce>() {
                @Override
                public void onResponse(Call<UniversitiesResponce> call, retrofit2.Response<UniversitiesResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UniversitiesResponce> call, Throwable t) {
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchUniversities(final Activity activity, final String word, final ResponceCallback responceCallback, String page) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            Log.d("AT@Dashboard", accessToken);
            final Call<UniversitiesResponce> call_register = api.searchUniversities(user_id, accessToken, Config.API_KEY, word, "en", page);
            call_register.enqueue(new Callback<UniversitiesResponce>() {

                @Override
                public void onResponse(Call<UniversitiesResponce> call, retrofit2.Response<UniversitiesResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UniversitiesResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTuitionfeesoption(final Activity contetxt, String userId,String degreeId/*,String courseid*/,String countryId, String accessToken, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<FeesResponse> call_register = apiInterface.getTuitionfeesoption(userId,degreeId/*,courseid*/,countryId, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<FeesResponse>() {

                @Override
                public void onResponse(Call<FeesResponse> call, retrofit2.Response<FeesResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }
                @Override
                public void onFailure(Call<FeesResponse> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getDegrees(final Activity contetxt, String userId, String accessToken, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<DegreeResponce> call_register = apiInterface.getDegrees(userId, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<DegreeResponce>() {

                @Override
                public void onResponse(Call<DegreeResponce> call, retrofit2.Response<DegreeResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<DegreeResponce> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCourses(final Activity contetxt, String userId, String accessToken, String degreeId, String nextPage, String keyword, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<CourseResponce> call_register = apiInterface.getCourse(userId, degreeId, accessToken, Config.API_KEY, nextPage, keyword);
            call_register.enqueue(new Callback<CourseResponce>() {

                @Override
                public void onResponse(Call<CourseResponce> call, retrofit2.Response<CourseResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<CourseResponce> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getNewCourses(final Activity contetxt, String userId, String accessToken, String degreeId,String courseType, String nextPage, String keyword, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<CourseResponce> call_register = apiInterface.getNewCourse(userId, degreeId, courseType,accessToken, Config.API_KEY, nextPage, keyword);
            call_register.enqueue(new Callback<CourseResponce>() {

                @Override
                public void onResponse(Call<CourseResponce> call, retrofit2.Response<CourseResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<CourseResponce> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCourceDetailsUniDetailsAPi(final Activity contetxt, String userId, String accessToken, String degreeId,String courseType/*,String streamId*/,String uniId, String nextPage, String keyword, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<CourseResponce> call_register = apiInterface.getCourseForUniDetails(userId, degreeId, courseType,accessToken, Config.API_KEY, nextPage, keyword,/*streamId,*/uniId);
            call_register.enqueue(new Callback<CourseResponce>() {

                @Override
                public void onResponse(Call<CourseResponce> call, retrofit2.Response<CourseResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<CourseResponce> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getCountries(final Activity contetxt, String userId, String accessToken, String degreeId/*, String courseId*/, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<CountryResponce> call_register = apiInterface.getCountries(userId, degreeId/*, courseId*/, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<CountryResponce>() {

                @Override
                public void onResponse(Call<CountryResponce> call, retrofit2.Response<CountryResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<CountryResponce> call, Throwable t) {
                    Utility.showToast(contetxt, contetxt.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get University by Degree , Course & country
    public static void getUniversities(final Activity context, String userId, String accessToken, String degreeId/*, String courseId*/, String countryId, String fees,final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Log.d("AT@Dashboard", accessToken);
            final Call<UniversitiesResponce> responceCall = api.getUniversity(userId, degreeId/*, courseId*/, countryId, accessToken, Config.API_KEY,fees);
            responceCall.enqueue(new Callback<UniversitiesResponce>() {

                @Override
                public void onResponse(Call<UniversitiesResponce> call, retrofit2.Response<UniversitiesResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UniversitiesResponce> call, Throwable t) {
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPackages(final ResponceCallback responceCallback,String language) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<PackagesResponse> responceCall = api.getPackages(accessToken,language);

            responceCall.enqueue(new Callback<PackagesResponse>() {

                @Override
                public void onResponse(Call<PackagesResponse> call, retrofit2.Response<PackagesResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<PackagesResponse> call, Throwable t) {
//                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void getCheckoutId(final Activity activity, final String price, final String shopperId, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();

            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<CheckOutResponse> call = api.getCheckOutId(user_id, price, shopperId);
            call.enqueue(new Callback<CheckOutResponse>() {
                @Override
                public void onResponse(Call<CheckOutResponse> call, Response<CheckOutResponse> response) {
                    responceCallback.onSuccessResponce(response);
                }

                @Override
                public void onFailure(Call<CheckOutResponse> call, Throwable t) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
    }
    public static void removeapplication(final Activity activity, final String applyId, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<GenericResponce> call = api.removeAppliedUniversity(user_id, accessToken, applyId,Config.API_KEY);
            call.enqueue(new Callback<GenericResponce>() {
                @Override
                public void onResponse(Call<GenericResponce> call, Response<GenericResponce> response) {
                    responceCallback.onSuccessResponce(response);
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
    }

    // Get All University
    public static void getAllUniversities(final Activity activity, final ResponceCallback responceCallback, String currnetPage) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            Log.d("AT@Dashboard", accessToken);
            final Call<UniversitiesResponce> call_register = api.getAllUniversities(user_id, accessToken, Config.API_KEY, "en", currnetPage);
            call_register.enqueue(new Callback<UniversitiesResponce>() {

                @Override
                public void onResponse(Call<UniversitiesResponce> call, retrofit2.Response<UniversitiesResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UniversitiesResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTokenKeys(final Activity activity, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            Log.d("AT@Dashboard", accessToken);
            final Call<TokenLiveResponse> call_register = api.getToken(accessToken);
            call_register.enqueue(new Callback<TokenLiveResponse>() {

                @Override
                public void onResponse(Call<TokenLiveResponse> call, retrofit2.Response<TokenLiveResponse> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<TokenLiveResponse> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateProfile(final Activity context, final ProgressDialog dialog, String dialogMsg, HashMap<String, String> paramMap, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, dialogMsg, context);

            final Call<UpdateResponse> call_register = api.updateProfile(paramMap);
            call_register.enqueue(new Callback<UpdateResponse>() {

                @Override
                public void onResponse(Call<UpdateResponse> call, retrofit2.Response<UpdateResponse> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UpdateResponse> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }
    public static void changeNotificationStatus(final Activity context, final ProgressDialog dialog, String dialogMsg, HashMap<String, String> paramMap, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, dialogMsg, context);

            final Call<UpdateResponse> call_register = api.changeNotificationStatus(paramMap);
            call_register.enqueue(new Callback<UpdateResponse>() {

                @Override
                public void onResponse(Call<UpdateResponse> call, retrofit2.Response<UpdateResponse> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UpdateResponse> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog(dialog);
        }
    }
    public static void logout(final Activity activity, final ProgressDialog dialog, String msg, final ResponceCallback responceCallback) {
        try {
            Utility.showProgressDialog(dialog, msg, activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<GenericResponce> call_register = api.logout(user_id, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void contactUsCall(final Activity activity, final ProgressDialog dialog, String username,String email,String message, final ResponceCallback responceCallback) {
        try {
            Utility.showProgressDialog(dialog, "Please wait..", activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<GenericResponce> call_register = api.contactUs(user_id, accessToken, Config.API_KEY,username,email,message);
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void checkApplyFor(final Activity activity, final ProgressDialog dialog, String msg, final ResponceCallback responceCallback, String universityId, String degreeId, String courseId, String countryId) {
        try {
            Utility.showProgressDialog(dialog, msg, activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<GenericResponce> call_register = api.CheckApplyFor(Config.API_KEY, user_id, accessToken, universityId, degreeId, courseId, countryId);
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void uploadImage(final Activity activity, File file, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();

            RequestBody userId_ = toRequestBody(user_id);
            RequestBody accessKey_ = toRequestBody(accessToken);
            RequestBody API_KEY_ = toRequestBody(Config.API_KEY);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            final Call<UploadFileResponce> call_register = api.uploadImage(userId_, accessKey_, API_KEY_, body);
            call_register.enqueue(new Callback<UploadFileResponce>() {
                @Override
                public void onResponse(Call<UploadFileResponce> call, retrofit2.Response<UploadFileResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UploadFileResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadEditImage(final Activity activity, File file, String type, String applyId, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();

            RequestBody userId_ = toRequestBody(user_id);
            RequestBody accessKey_ = toRequestBody(accessToken);
            RequestBody API_KEY_ = toRequestBody(Config.API_KEY);
            RequestBody bodyType_ = toRequestBody(type);
            RequestBody bodyApplyId = toRequestBody(applyId);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            final Call<UploadFileResponce> call_register = api.uploadEditImage(userId_, accessKey_, API_KEY_, bodyType_, bodyApplyId, body);
            call_register.enqueue(new Callback<UploadFileResponce>() {
                @Override
                public void onResponse(Call<UploadFileResponce> call, retrofit2.Response<UploadFileResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<UploadFileResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void ApplyApplication(final Activity activity, final ProgressDialog dialog, final ResponceCallback responceCallback, String commaSeperatedIDs, String commaSeperatedName, String commaSeperatedPrice, String qty, String successPaymentId,String paymentstatus) {
        try {
            Utility.showProgressDialog(dialog, "Applying ... ", activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String accessToken = SharedClass.userModel.getAccessKey();
            String ApiKey = Config.API_KEY;

            int uniId = ApplyModel.universityModel.getId();
            String user_id = SharedClass.userModel.getId().toString();

            String highSchoolTranscript = "";
            String recommendationLetters = "";
            String passportCopies = "";
            String englishProfeciencyTest = "";
            String personalStatment = "";
            String extraDocuments = "";

            if (ApplyModel.highSchoolTranscript != null) {
                highSchoolTranscript = GenerateCommaSeperatedString(ApplyModel.highSchoolTranscript);
            }
            if (ApplyModel.recommendationLetters != null) {
                recommendationLetters = GenerateCommaSeperatedString(ApplyModel.recommendationLetters);
            }
            if (ApplyModel.passportCopies != null) {
                passportCopies = GenerateCommaSeperatedString(ApplyModel.passportCopies);
            }
            if (ApplyModel.englishProfeciencyTest != null) {
                englishProfeciencyTest = GenerateCommaSeperatedString(ApplyModel.englishProfeciencyTest);
            }
            if (ApplyModel.personalStatment != null) {
                personalStatment = GenerateCommaSeperatedString(ApplyModel.personalStatment);
            }
            if (ApplyModel.extraDocuments != null) {
                extraDocuments = GenerateCommaSeperatedString(ApplyModel.extraDocuments);
            }

            HashMap<String, String> map = new HashMap<>();

            map.put(Constants.ACCESS_KEY, accessToken);
            map.put(Constants.COUNTRY_ID, ApplyModel.countryId);
            map.put(Constants.API_KEY, ApiKey);
            map.put(Constants.payment_status, paymentstatus);
            map.put(Constants.COURSE_ID, ApplyModel.courseId);
            map.put(Constants.STREM_ID,String.valueOf(0));
            map.put(Constants.UNIVERSITY_ID, uniId + "");
//            map.put(Constants.STREM_ID, ApplyModel.streamId);
            map.put(Constants.USER_ID, user_id);
            map.put(Constants.Eng_Proficieny_Tests_KEY, englishProfeciencyTest);
            map.put(Constants.DEGREE_ID, ApplyModel.degreeId);
            map.put(Constants.Recommendation_Letters_KEY, recommendationLetters);
            map.put(Constants.High_School_Transcripts_KEY, highSchoolTranscript);
            map.put(Constants.Passport_Copies_KEY, passportCopies);
            map.put(Constants.Extra_Document_KEY, extraDocuments);
            map.put(Constants.Personal_Statement_KEY, personalStatment);
            map.put(Constants.packages_id, commaSeperatedIDs);
            map.put(Constants.name, commaSeperatedName);
            map.put(Constants.price, commaSeperatedPrice);
            map.put(Constants.qty, qty);
            map.put(Constants.success_payments_id, successPaymentId);
            map.put(Constants.apply_type, "Y");
            map.put(Constants.PAYMENT_COMMENT, PAYMENT_COMMENT_TEXT);

            final Call<GenericResponce> call_register = api.ApplyForApplication(map);
            call_register.enqueue(new Callback<GenericResponce>() {
                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        PAYMENT_COMMENT_TEXT = "";
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);

                        PAYMENT_COMMENT_TEXT = "";
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UpdateApplyApplication(final Activity activity, final ProgressDialog dialog, final ResponceCallback responceCallback, String commaSeperatedIDs, String commaSeperatedName, String commaSeperatedPrice, String qty, String successPaymentId, String applyId) {
        try {
            Utility.showProgressDialog(dialog, "Applying ... ", activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String accessToken = SharedClass.userModel.getAccessKey();
            String ApiKey = Config.API_KEY;

            String user_id = SharedClass.userModel.getId().toString();

            HashMap<String, String> map = new HashMap<>();
            map.put(Constants.ACCESS_KEY, accessToken);
            map.put(Constants.API_KEY, ApiKey);
            map.put(Constants.USER_ID, user_id);
            map.put(Constants.packages_id, commaSeperatedIDs);
            map.put(Constants.name, commaSeperatedName);
            map.put(Constants.price, commaSeperatedPrice);
            map.put(Constants.qty, qty);
            map.put(Constants.success_payments_id, successPaymentId);
            map.put(Constants.APPLIES_ID, applyId);
            map.put(Constants.PAYMENT_COMMENT, PAYMENT_COMMENT_TEXT);


            final Call<GenericResponce> call_register = api.UpdateApplyForApplication(map);
            call_register.enqueue(new Callback<GenericResponce>() {
                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        PAYMENT_COMMENT_TEXT = "";
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                        PAYMENT_COMMENT_TEXT = "";
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    //  Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ApplyNewApplication(final Activity activity, final ProgressDialog dialog, final ResponceCallback responceCallback, String commaSeperatedIDs, String commaSeperatedName, String commaSeperatedPrice, String qty, String successPaymentId, String applyType) {
        try {
            Utility.showProgressDialog(dialog, "Applying ... ", activity);
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String accessToken = SharedClass.userModel.getAccessKey();
            String ApiKey = Config.API_KEY;


            String user_id = SharedClass.userModel.getId().toString();


            HashMap<String, String> map = new HashMap<>();

            map.put(Constants.ACCESS_KEY, accessToken);
            map.put(Constants.COUNTRY_ID, "-");
            map.put(Constants.API_KEY, ApiKey);
            map.put(Constants.COURSE_ID, "-");
            map.put(Constants.UNIVERSITY_ID, "-");
            map.put(Constants.USER_ID, user_id);
            map.put(Constants.Eng_Proficieny_Tests_KEY, "-");
            map.put(Constants.DEGREE_ID, "-");
            map.put(Constants.Recommendation_Letters_KEY, "-");
            map.put(Constants.High_School_Transcripts_KEY, "-");
            map.put(Constants.Passport_Copies_KEY, "-");
            map.put(Constants.Personal_Statement_KEY, "-");
            map.put(Constants.Extra_Document_KEY, "-");
            map.put(Constants.packages_id, commaSeperatedIDs);
            map.put(Constants.name, commaSeperatedName);
            map.put(Constants.price, commaSeperatedPrice);
            map.put(Constants.qty, qty);
            map.put(Constants.success_payments_id, successPaymentId);
            map.put(Constants.apply_type, applyType);
            map.put(Constants.PAYMENT_COMMENT, PAYMENT_COMMENT_TEXT);

            final Call<GenericResponce> call_register = api.ApplyForApplication(map);
            call_register.enqueue(new Callback<GenericResponce>() {
                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        PAYMENT_COMMENT_TEXT = "";

                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                        PAYMENT_COMMENT_TEXT = "";
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String GenerateCommaSeperatedString(ArrayList<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + ",";
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public static void getMyApplication(final Activity activity, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);

            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<ApplicationResponce> call_register = apiInterface.GetMyApplications(user_id, accessToken, Config.API_KEY);
            call_register.enqueue(new Callback<ApplicationResponce>() {

                @Override
                public void onResponse(Call<ApplicationResponce> call, retrofit2.Response<ApplicationResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<ApplicationResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void FetchPreviousChat(final Activity activity, String currentPage, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<ChatResponce> call_register = api.fetchPreviousChat(user_id + "", accessToken, Config.API_KEY, currentPage);
            call_register.enqueue(new Callback<ChatResponce>() {

                @Override
                public void onResponse(Call<ChatResponce> call, retrofit2.Response<ChatResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<ChatResponce> call, Throwable t) {
                    Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateDeviceToken(String token) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(Constants.DEVICE_TOKEN, token);
            paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
            paramMap.put(Constants.DEVIC_TYPE, Config.device_type);
            paramMap.put(Constants.API_KEY, Config.API_KEY);
            paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
            paramMap.put(Constants.PUSH_MODE, "1");


            final Call<GenericResponce> call_register = api.updateDeviceToken(paramMap);
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    if (response.isSuccessful()) {
                        GenericResponce genericResponce = response.body();
                        String str = "ASd";
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLanguage(final Activity activity, String language, final ResponceCallback responceCallback) {
        ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Constants.LANGUAGE, language);
        paramMap.put(Constants.USER_ID, SharedClass.userModel.getId().toString());
        paramMap.put(Constants.API_KEY, Config.API_KEY);
        paramMap.put(Constants.ACCESS_KEY, SharedClass.userModel.getAccessKey());
        paramMap.put(Constants.PUSH_MODE, "1");

        final Call<GenericResponce> call_register = api.updateUserlanguage(paramMap);
        call_register.enqueue(new Callback<GenericResponce>() {

            @Override
            public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                if (response.isSuccessful()) {
                    responceCallback.onSuccessResponce(response);

                } else {
                    responceCallback.onFailureResponce(response);
                }
            }

            @Override
            public void onFailure(Call<GenericResponce> call, Throwable t) {
                Utility.showToast(activity, activity.getResources().getString(R.string.network_connectivity));
                responceCallback.onFailureResponce(call);
            }

        });
    }

    public static void ForgetPassword(final Activity activity, final ProgressDialog dialog, String email, final ResponceCallback responceCallback) {
        try {
            ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
            Utility.showProgressDialog(dialog, activity.getString(R.string.please_wait), activity);
            final Call<GenericResponce> call_register = apiInterface.ForgetPassword(email,SessionManager.get(Constants.LANGUAGE));
            call_register.enqueue(new Callback<GenericResponce>() {

                @Override
                public void onResponse(Call<GenericResponce> call, retrofit2.Response<GenericResponce> response) {
                    Utility.dismissProgressDialog(dialog);
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);
                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponce> call, Throwable t) {
                    Utility.dismissProgressDialog(dialog);
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            Utility.dismissProgressDialog(dialog);
            e.printStackTrace();
        }
    }

    public static void getBanners(final Activity context, final ResponceCallback responceCallback) {

        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            final Call<BannerResponce> call_register = api.getBanner();
            call_register.enqueue(new Callback<BannerResponce>() {

                @Override
                public void onResponse(Call<BannerResponce> call, retrofit2.Response<BannerResponce> response) {
                    if (response.isSuccessful()) {
                        responceCallback.onSuccessResponce(response);

                    } else {
                        responceCallback.onFailureResponce(response);
                    }
                }

                @Override
                public void onFailure(Call<BannerResponce> call, Throwable t) {
                    Utility.showToast(context, context.getResources().getString(R.string.network_connectivity));
                    responceCallback.onFailureResponce(call);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPaymentStatus(final Activity activity, final String checkId, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            Log.d("ZeroUserId", user_id);
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<GetPaymentResponse> call = api.getPaymentStatus(checkId, user_id);
            call.enqueue(new Callback<GetPaymentResponse>() {

                @Override
                public void onResponse(Call<GetPaymentResponse> call, Response<GetPaymentResponse> response) {
                    responceCallback.onSuccessResponce(response);
                }

                @Override
                public void onFailure(Call<GetPaymentResponse> call, Throwable t) {
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
    }


    public static void savePayment(final Activity activity, String response, final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<SavePaymentResponse> call = api.savePayment(Constants.CHECKOUTID, user_id, response, Constants.PACKAGEID, Constants.PRICE);
            call.enqueue(new Callback<SavePaymentResponse>() {
                @Override
                public void onResponse(Call<SavePaymentResponse> call, Response<SavePaymentResponse> response) {
                    responceCallback.onSuccessResponce(response);
                }

                @Override
                public void onFailure(Call<SavePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
    }

    public static void saveCODPayment(final Activity activity, String checkoutId,String response,String packageId,String amount ,final ResponceCallback responceCallback) {
        try {
            ApiInterface api = ApiInterface.retrofit.create(ApiInterface.class);
            String user_id = SharedClass.userModel.getId().toString();
            String accessToken = SharedClass.userModel.getAccessKey();
            final Call<SavePaymentResponse> call = api.savePayment(checkoutId, user_id, response, packageId, amount);
            call.enqueue(new Callback<SavePaymentResponse>() {
                @Override
                public void onResponse(Call<SavePaymentResponse> call, Response<SavePaymentResponse> response) {
                    responceCallback.onSuccessResponce(response);
                }

                @Override
                public void onFailure(Call<SavePaymentResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {

        }
    }
}
