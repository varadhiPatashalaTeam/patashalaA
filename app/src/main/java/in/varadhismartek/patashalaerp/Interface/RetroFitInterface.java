package in.varadhismartek.patashalaerp.Interface;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroFitInterface {
    @FormUrlEncoded
    @POST("school_login")
    Call<JsonElement> loginWithPassword(@Field("mobile_number")String mobile_number,
                                        @Field("password")String password);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<JsonElement> forgot_pass(@Field("mobile_number")String mobile_number);



    @FormUrlEncoded
    @POST("login_school_otp")
    Call<JsonElement> loginWithOtp(@Field("mobile_number") String mobile_number,
                                     @Field("otp_password")String otp_password);


    @FormUrlEncoded
    @POST("change_password")
    Call<JsonElement> change_password(@Field("mobile_number") String mobile_number,
                                      @Field("otp_password")String otp_password);

    @FormUrlEncoded
    @POST("set_the_barriers_and_employee")
    Call<JsonElement> getEmployeeId(
            @Field("school_id") String schoolId,
            @Field("poc_mobile_number") String mobileNo,
            @Field("poc_name") String empName);


}

