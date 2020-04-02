package in.varadhismartek.patashalaerp.LoginAndRegistation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.Mpin;
import in.varadhismartek.patashalaerp.Interface.RetroFitInterface;
import in.varadhismartek.patashalaerp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdOTPFragment extends Fragment implements View.OnClickListener {


    Boolean flag_to_disable_resend=false;
    RetroFitInterface retroFitInterface;
    ProgressDialog dialog;
    private static ThirdOTPFragment ins;
    String phoneNum = "";
    long timeFlag=0;


    Mpin                   otp_pin;

    Button                 btn_next;

    TextView               step_number_1,step_number_2,timely_text;




    public ThirdOTPFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_otp, container, false);
        ins = this;
        retroFitInterface= SecondEnterMobileFragment.RetrofitInstantClient.getRetrofitInstance().create(RetroFitInterface.class);

        Log.i("str88888896", "" + Constant.SCHOOL_ID);
        Log.i("str88888896", "" + Constant.EMPLOYEE_BY_ID);
        Log.i("str88888896", "" + Constant.SCHOOL_LOGO);
        Log.i("str88888896", "" + Constant.POC_NAME);
        Log.i("str88888896", "" + Constant.poc_mob);
        Log.i("str88888896", "" + Constant.POC_Mobile_Number);
        Log.i("str88888896", "" + Constant.EMPLOYEE_FNAME);
        Log.i("str88888896", Constant.EMPLOYEE_LNAME);
        Log.i("str88888896", Constant.EMPLOYEE_ADDHAR);
        Log.i("str88888896", Constant.EMPLOYEE_ROLE);
        Log.i("str88888896", Constant.EMPLOYEE_DEPARTMENT);
        Log.i("str88888896", Constant.EMPLOYEE_WING);

        initializeViews(v);
        intializeSetOnClick();
        setInitialVisiualFormat();
        phoneNum=Constant.mobile_number;
        startCountDownTimer();



        return v;
    }
    public static ThirdOTPFragment getinstance(){
        return ins;
    }




    private void intializeSetOnClick() {
        btn_next.setOnClickListener(this);

    }

    private void initializeViews(View v) {

        step_number_1=v.findViewById(R.id.number1);
        step_number_2=v.findViewById(R.id.number2);
        timely_text=v.findViewById(R.id.timely_text);

        btn_next=v.findViewById(R.id.submit_otp);


        //tv_MobileNumber  = v.findViewById(R.id.textViewMobileNumber);
        otp_pin             = v.findViewById(R.id.mpin_reset_password);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait...");
        // btn_submitOtp    = v.findViewById(R.id.otp_ok);
        //btn_resendOtp    = v.findViewById(R.id.otp_resendCode);

    }
    private void setInitialVisiualFormat(){

        step_number_1.setText(getString(R.string.tick_mark));
        step_number_1.setTextColor(getResources().getColor(R.color.white));
        step_number_1.setBackgroundResource(R.drawable.step_completed);
        step_number_2.setBackgroundResource(R.drawable.step_selected);
        //tv_MobileNumber.setText(Constant.maskingNumber);

    }
    //Timer to wait to resend the otp
    private void startCountDownTimer(){
        new CountDownTimer(300000, 1000) {

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                timeFlag=millisUntilFinished/60000+1;
                timely_text.setText("Request new OTP in "+String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                try {
                    timely_text.setText(getString(R.string.click_to_resend_otp));
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
                    timely_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!flag_to_disable_resend){
                                flag_to_disable_resend=true;
                                startCountDownTimer();
                                Call<JsonElement> call2=retroFitInterface.forgot_pass(Constant.mobile_number);
                                call2.enqueue(new Callback<JsonElement>() {
                                    @Override
                                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                        dialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                                            String str = jsonObject.getString(Constant.STATUS);
                                            switch (str){
                                                case Constant.SUCCESS:
                                                    dialog.dismiss();
                                                    LoginScreenActivity loginScreenActivity2 = (LoginScreenActivity) getActivity();
                                                    assert loginScreenActivity2 != null;
                                                    loginScreenActivity2.loadFragment(Constant.THIRD_OTP_ENTER_FRAGMENT, null);
                                                    break;
                                                case Constant.MOBILE_DOESNOT_EXIST:
                                                    dialog.dismiss();
                                                    new AlertDialog.Builder(getContext())
                                                            .setTitle(getString(R.string.oops))
                                                            .setMessage(getString(R.string.mobile_not_registered))
                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                                            .setNegativeButton(android.R.string.no, null)
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                    break;
                                                default:
                                                    dialog.dismiss();
                                                    new AlertDialog.Builder(getContext())
                                                            .setTitle(getString(R.string.oops))
                                                            .setMessage(getString(R.string.something_is_wrong))
                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                                            .setNegativeButton(android.R.string.no, null)
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                    break;
                                            }

                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonElement> call, Throwable t) {

                                    }
                                });




                            }else {
                                timely_text.setText(getString(R.string.try_later));
                            }

                        }
                    });
                }


        }.start();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.submit_otp:
                if (otp_pin.getValue().length()<6){
                    Toast.makeText(getContext(),getString(R.string.please_type_verification_code),Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.setMessage(getString(R.string.authenticating_please_wait));
                dialog.show();
                Call<JsonElement> call=retroFitInterface.loginWithOtp(Constant.mobile_number,otp_pin.getValue());
                Log.d("inputs",otp_pin.getValue());
                Log.d("mob",Constant.mobile_number);

                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            String str = jsonObject.getString(Constant.STATUS);
                            Log.d("response",""+response.body());
                            switch (str){
                                case Constant.SUCCESS:
                                    LoginScreenActivity loginScreenActivity2 = (LoginScreenActivity) getActivity();
                                    assert loginScreenActivity2 != null;
                                    loginScreenActivity2.loadFragment(Constant.FOURTH_MPIN_FRAGMENT, null);
                                    break;
                                case Constant.MOBILE_DOESNOT_EXIST:
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(getString(R.string.oops))
                                            .setMessage(getString(R.string.mobile_not_registered))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                    break;
                                default:
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(getString(R.string.oops))
                                            .setMessage(getString(R.string.something_is_wrong))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    LoginScreenActivity loginScreenActivity2 = (LoginScreenActivity) getActivity();
                                                    assert loginScreenActivity2 != null;
                                                    loginScreenActivity2.loadFragment(Constant.SECOND_SEARCH_USER_FRAGMENT, null);

                                                }
                                            })
                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                    break;

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                });

                break;

            case R.id.otp_resendCode:
                break;

        }
    }




    public void SetTextToMpin(String str){
        Log.d("recived",""+str);
        otp_pin.setValue(str);

    }

}
