package in.varadhismartek.patashalaerp.LoginAndRegistation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.Mpin;
import in.varadhismartek.patashalaerp.GeneralClass.PasscodeView;
import in.varadhismartek.patashalaerp.R;

public class ForgetResetMpinActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_forgetMpinLayout, ll_resetMpinLayout,llResetMpinView,llOtpEnter;
    Mpin oldResetMpinView, newResetMpinView, confirmResetMpin,
            forgetOtpMpinView, newForgetMpinView, confirmForgetMpinView;
    Button btnSaveResetMpin,btnSaveForgetMpin;
    SharedPreferences defaultStatusForSecurity = null;


    private FrameLayout mFrameCloseKeyboard;

    private FrameLayout mFrameNumber1;
    private FrameLayout mFrameNumber2;
    private FrameLayout mFrameNumber3;
    private FrameLayout mFrameNumber4;
    private FrameLayout mFrameNumber5;
    private FrameLayout mFrameNumber6;
    private FrameLayout mFrameNumber7;
    private FrameLayout mFrameNumber8;
    private FrameLayout mFrameNumber9;
    private FrameLayout mFrameNumber0;
    private FrameLayout mFrameNumberDeleteSpace;
    private FrameLayout mFrameNumberNext;

    private List<String> mListPin;

    private TextView mPin1;
    private TextView mPin2;
    private TextView mPin3;
    private TextView mPin4;

    PasscodeView passcode_View;





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.FORGET_RESET_MPIN.equals("RESET")){

            setContentView(R.layout.reset_mpin_layout);

        }
        else {
            setContentView(R.layout.otp_layout);

        }



        defaultStatusForSecurity = this.getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);

        //initialializeAllViews();

        //getStatusForgetOrReset();


        //-------------------------------------------------

        mFrameCloseKeyboard = findViewById(R.id.frameLayout_close_keyboard);

        mFrameNumber1 = findViewById(R.id.frameLayout_number1);
        mFrameNumber2 = findViewById(R.id.frameLayout_number2);
        mFrameNumber3 = findViewById(R.id.frameLayout_number3);
        mFrameNumber4 = findViewById(R.id.frameLayout_number4);
        mFrameNumber5 = findViewById(R.id.frameLayout_number5);
        mFrameNumber6 = findViewById(R.id.frameLayout_number6);
        mFrameNumber7 = findViewById(R.id.frameLayout_number7);
        mFrameNumber8 = findViewById(R.id.frameLayout_number8);
        mFrameNumber9 = findViewById(R.id.frameLayout_number9);
        mFrameNumber0 = findViewById(R.id.frameLayout_number0);
        mFrameNumberDeleteSpace = findViewById(R.id.frameLayout_deletePin);
        mFrameNumberNext = findViewById(R.id.frameLayout_next);
        //tvYourPIN        =  v.findViewById(R.id.textview_your_pin);

        mPin1 = findViewById(R.id.textView_pin1);
        mPin2 = findViewById(R.id.textView_pin2);
        mPin3 = findViewById(R.id.textView_pin3);
        mPin4 = findViewById(R.id.textView_pin4);



        llResetMpinView = findViewById(R.id.ll_resetMpinLayout);
        llOtpEnter = findViewById(R.id.llOtpEnter);

        mFrameCloseKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });

        setPin();




    }

    private void setPin() {

        mListPin = new ArrayList<>();

        mFrameNumber1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("1");
                    conditioningPinButton();
                }else{

                }

            }
        });

        mFrameNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("2");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("3");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("4");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("5");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("6");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("7");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("8");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("9");
                    //Toast.makeText(InputPinActivity.this, "Size : "+ mListPin.size(), Toast.LENGTH_LONG).show();
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumber0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size() <=3){
                    mListPin.add("0");
                    conditioningPinButton();
                }else{

                }
            }
        });

        mFrameNumberDeleteSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListPin.size() != 0){
                    mListPin.remove(mListPin.size()-1);
                    if(mListPin.size()==3){
                        mPin4.setBackgroundResource(R.drawable.non_selected_item);
                    }else if(mListPin.size()==2){
                        mPin3.setBackgroundResource(R.drawable.non_selected_item);
                    }else if(mListPin.size()==1){
                        mPin2.setBackgroundResource(R.drawable.non_selected_item);
                    }else if(mListPin.size()==0){
                        mPin1.setBackgroundResource(R.drawable.non_selected_item);
                    }
                }
            }
        });

        mFrameNumberNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPin.size()<4){
                    Toast.makeText(ForgetResetMpinActivity.this, R.string.msg_complete_your_pin, Toast.LENGTH_LONG).show();
                    return;
                }
                //print your PIN
                String yourPin = "";
                for(int i=0; i<mListPin.size(); i++){
                    yourPin += mListPin.get(i);
                }
                //tvYourPIN.setText("Your PIN : " + yourPin);

                if(!yourPin.equals(Constant.MPIN)){
                    Toast.makeText(ForgetResetMpinActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                }
                else {


                    llOtpEnter.setVisibility(View.GONE);
                    llResetMpinView.setVisibility(View.VISIBLE);
                    passcode_View = findViewById(R.id.resetMpinView);
                    passcode_View.setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {


                        }

                        @Override
                        public void onSuccess(String number) {
                            Toast.makeText(ForgetResetMpinActivity.this, "MPIN Resetted", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor edt = defaultStatusForSecurity.edit();
                            edt.putString("MPINUSER", number);
                            edt.apply();
                            Intent intent = new Intent(ForgetResetMpinActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }
                    });

                    Toast.makeText(ForgetResetMpinActivity.this, "CorrectPin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void conditioningPinButton() {
        if(mListPin.size()==1){
            mPin1.setBackgroundResource(R.drawable.selected_item);
        }else if(mListPin.size()==2){
            mPin2.setBackgroundResource(R.drawable.selected_item);
        }else if(mListPin.size()==3){
            mPin3.setBackgroundResource(R.drawable.selected_item);
        }else if(mListPin.size()==4){
            mPin4.setBackgroundResource(R.drawable.selected_item);
        }
    }

    /*private void initialializeAllViews() {
        ll_forgetMpinLayout = findViewById(R.id.forgetMpinLayout);
        ll_resetMpinLayout = findViewById(R.id.resetMpinLayout);
        oldResetMpinView = findViewById(R.id.oldMpin);
        newResetMpinView = findViewById(R.id.newMpin);
        confirmResetMpin = findViewById(R.id.confirmMpin);
        btnSaveResetMpin = findViewById(R.id.btnSaveReset);
        btnSaveForgetMpin = findViewById(R.id.btnSaveForget);
        forgetOtpMpinView = findViewById(R.id.otpForgetMpin);
        newForgetMpinView = findViewById(R.id.forgetNewMpin);
        confirmForgetMpinView = findViewById(R.id.forgetConfirmMpin);


        if(oldResetMpinView.getValue().length()==4 || oldResetMpinView.getValue().equals(Constant.MPIN)){

            Toast.makeText(ForgetResetMpinActivity.this, "Okay", Toast.LENGTH_SHORT).show();
        }


        btnSaveResetMpin.setOnClickListener(this);
        btnSaveForgetMpin.setOnClickListener(this);


    }*/

    private void getStatusForgetOrReset() {
        switch (Constant.FORGET_RESET_MPIN){
            case "FORGET":

                break;

            case "RESET":

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

           /* case R.id.btnSaveReset:
                if(oldResetMpinView.getValue().length()!=4){
                    Toast.makeText(this, "Fill Old MPIN properly", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(newResetMpinView.getValue().length()!=4) {
                    Toast.makeText(this, "Fill New MPIN properly", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(confirmResetMpin.getValue().length()!=4) {
                    Toast.makeText(this, "Fill Confirm MPIN properly", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!oldResetMpinView.getValue().equals(Constant.MPIN)){
                    Toast.makeText(this, "Wrong Old MPIN", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!newResetMpinView.getValue().equals(confirmResetMpin.getValue())){
                    Toast.makeText(this, "MPIN not matches", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    SharedPreferences.Editor edt = defaultStatusForSecurity.edit();
                    edt.putString("MPINUSER", newResetMpinView.getValue());
                    edt.apply();
                    Toast.makeText(this, "MPIN RESETTED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    this.startActivity(intent);
                    this.finishAffinity();

                }

                break;
*/

            /*case R.id.btnSaveForget:
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
