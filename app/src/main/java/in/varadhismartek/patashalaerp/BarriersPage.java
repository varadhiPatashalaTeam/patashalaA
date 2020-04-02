package in.varadhismartek.patashalaerp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.GeneralClass.Mpin;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.varadhismartek.Utils.Constant.POC_Mobile_Number;

public class BarriersPage {

    Context mContext;
    Dialog dialog = null;
    Utilities utilities;
    APIService mApiService;
    Mpin mpin;
    ImageView iv_logo, iv_cancel;
    Button btnOkOtp, btnResendOtp;
    TextView tv_OtpMobileNo;
    private String POC_Mobile_Number;
    ProgressDialog progressDialog;

    public BarriersPage(Context mContext) {
        this.mContext = mContext;
        mApiService = ApiUtils.getAPIService();
    }

    public void openOTPDialog(final Context mContext) {

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.otp_dialog_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.white_trans)));
        dialog.show();


        mpin = dialog.findViewById(R.id.mPinOtp);
        btnOkOtp = dialog.findViewById(R.id.otp_ok);
        iv_logo = dialog.findViewById(R.id.iv_logo);
        iv_cancel = dialog.findViewById(R.id.iv_cancel);
        tv_OtpMobileNo = dialog.findViewById(R.id.tv_OtpMobileNo);
        btnResendOtp = dialog.findViewById(R.id.otp_resendCode);

        dialog.setCancelable(true);

        Picasso.with(mContext).
                load(Constant.IMAGE_LINK + Constant.SCHOOL_LOGO)
                .placeholder(R.drawable.patashala_logo)
                .into(iv_logo);


        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    getOptToLogin();
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.displayProgressDialog(mContext, "Processing...", false);

                getOptToLogin();
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnOkOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((mpin.getValue().length() < 4)) {
                    Toast.makeText(mContext, "Enter valid otp number", Toast.LENGTH_SHORT).show();
                } else {
                    String strOtpNo = mpin.getValue();
                    utilities.displayProgressDialog(mContext, "Processing...", false);
                    getLoginWithOtp(strOtpNo);
                }
            }
        });


    }

    private void getOptToLogin() {

        //POC_Mobile_Number ="9964640238";

        final String lastFourDigits = Constant.POC_Mobile_Number.substring(Constant.POC_Mobile_Number.length() - 4);
        Log.i("POC_Mobile_Number***",""+Constant.POC_Mobile_Number+"**"+lastFourDigits);
        utilities.displayProgressDialog(mContext,"Process ...",false);

        mApiService.getOtpToLogin(Constant.POC_Mobile_Number).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {

                    try {
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (message1.equalsIgnoreCase("OTP sent successfully")) {
                            tv_OtpMobileNo.setText("Please type the verification code sent to \n +91 ******"+lastFourDigits);
                            //Toast.makeText(getActivity(), "OTP Send ******"+lastFourDigits+" ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(mContext, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();

                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API12", message);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.i("ERROR", "" + t.toString());
                utilities.cancelProgressDialog();

            }
        });
    }

    private void getLoginWithOtp(String strOtpNo) {

        mApiService.schoolLoginWithOtp(Constant.POC_Mobile_Number, strOtpNo).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    utilities.cancelProgressDialog();

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {
                            Toast.makeText(mContext, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            Intent intent = new Intent(mContext, DashboardSettingActivity.class);
                            mContext.startActivity(intent);


                        }


                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();

                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                utilities.cancelProgressDialog();
                Log.i("ADMIN_API_7", t.toString());

            }
        });
    }
}
