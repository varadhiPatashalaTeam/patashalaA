package in.varadhismartek.patashalaerp.LoginAndRegistation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref = null;
    SharedPreferences prefForDefaultMpin = null;

    SharedPreferences storeStaffTypeandRegistrationNumber = null;
    Dialog dialogMpin = null;
    Dialog dialogFinger = null;

    SharedPreferences defaultStatusForSecurity = null;
    EditText pinView;
    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_NAME = "AndroidExamples";
    private KeyStore keyStore;
    private Cipher cipher;
    ImageView finger_Auth;
    boolean fingetStatus;
    View view;
    private String securityStatus;
    private TextView tv_useFingerStats;
    private View viewLine;
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
    ImageView overFlowMenuIcon;
    TextView forgetMpin, resetMpin;

    private TextView tvYourPIN;
    private TextView tv_useMPin;
    private View view_line_finger;
    private String userMpin;
    private ImageView ivFingerAuth;
    private String audienceType;
    private String school_id;
    private String management_id;
    private TextView userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        view = this.findViewById(android.R.id.content);
       /* pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", true);
        edt.apply();*/



        defaultStatusForSecurity = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        fingetStatus = (defaultStatusForSecurity.getBoolean("FingerPrintStatus", false));
        securityStatus = (defaultStatusForSecurity.getString("DefaultSOption", ""));
        userMpin = (defaultStatusForSecurity.getString("MPINUSER", ""));
        audienceType = (defaultStatusForSecurity.getString("Audience", ""));
        school_id = (defaultStatusForSecurity.getString("SchoolId", ""));
        management_id = (defaultStatusForSecurity.getString("ManagementID", ""));

        Constant.SCHOOL_ID = defaultStatusForSecurity.getString("SchoolId", "0");
        Constant.EMPLOYEE_BY_ID = defaultStatusForSecurity.getString("EmployeeId", "0");
        Constant.POC_NAME = defaultStatusForSecurity.getString("PocName", "0");
        Constant.POC_Mobile_Number = defaultStatusForSecurity.getString("PocMobileNo", "0");
        Constant.SCHOOL_LOGO = defaultStatusForSecurity.getString("SchoolLogo","" );

        Constant.EMPLOYEE_PHOTO = defaultStatusForSecurity.getString("EmployeePhoto","" );
        Constant.EMPLOYEE_FNAME = defaultStatusForSecurity.getString("EmployeeFname","" );
        Constant. EMPLOYEE_LNAME= defaultStatusForSecurity.getString("EmployeeLname","" );
        Constant.EMPLOYEE_ADDHAR = defaultStatusForSecurity.getString("EmployeeAdhar","" );
        Constant.EMPLOYEE_EMP_CUSTOM_ID = defaultStatusForSecurity.getString("EmployeeEmpCusID","" );
        Constant.EMPLOYEE_DEPARTMENT = defaultStatusForSecurity.getString("EmployeeDept","" );
        Constant.EMPLOYEE_ROLE = defaultStatusForSecurity.getString("EmployeeRole","" );
        Constant.EMPLOYEE_WING = defaultStatusForSecurity.getString("EmployeeWing","" );



        userData = findViewById(R.id.name);
        userData.setText("Hi you are "+audienceType+"\n"+" Your School Id "+school_id+"\n"+"Your Management Id "+management_id);


        boolean booleanStatus = defaultStatusForSecurity.getBoolean("activity_executed",false);
        Log.i("booleanStatus_HM",""+booleanStatus);

        Constant.MPIN = userMpin;
        Constant.SCHOOL_ID =school_id;

        Log.d("Constant.SCHOOL_ID", ""+Constant.SCHOOL_ID);
        Log.d("Constant.SCHOOL_ID", ""+Constant.EMPLOYEE_BY_ID);


        prefForDefaultMpin = getSharedPreferences("Dialog_Security", Context.MODE_PRIVATE);
        int counter = prefForDefaultMpin.getInt("counter", 0);

        if (counter == 0) {
            SharedPreferences.Editor et = prefForDefaultMpin.edit();
            et.putInt("counter", 1);
            et.apply();
        }
        else {


            if(securityStatus.equals("MPIN")){

                callMpinDialog();


            }
            else if(securityStatus.equals("FINGER")){

                callFingerDialog();

            }

            Log.d("countVal", "daasd");



        }


    }


    private void checkFingerPrint() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                Log.d("Finger hardware", "No");

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                Log.d("fingerNotErolled", "Yes");

            } else {
                // Everything is ready for fingerprint authentication
                Log.d("fingerAvailable", "Yes");


                generateKey();

                if (cipherInit()) {
                    FingerprintManager.CryptoObject cryptoObject =
                            new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler helper = new FingerprintHandler(HomeActivity.this, ivFingerAuth);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }


            }
        } else {

            Log.d("Fingerhardware", "No");

        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (fingetStatus) {
            checkFingerPrint();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                    + "/"
                    + KeyProperties.BLOCK_MODE_CBC
                    + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(
                    KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

        private Context context;
        private ImageView imageView;

        // Constructor
        private FingerprintHandler(Context mContext, ImageView imageView) {
            context = mContext;
            this.imageView = imageView;

        }

        private void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.USE_FINGERPRINT)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            this.update("Fingerprint Authentication error\n" + errString, false);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            this.update("Fingerprint Authentication help\n" + helpString, false);
        }

        @Override
        public void onAuthenticationFailed() {
            this.update("Fingerprint Authentication failed.", false);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            this.update("Fingerprint Authentication succeeded.", true);
        }

        private void update(String e, Boolean success) {
            if (success) {
                imageView.setColorFilter(HomeActivity.this.getResources().getColor(R.color.barrier_green_colorPrimary), PorterDuff.Mode.SRC_IN);
                dialogFinger.dismiss();

            }
            if (!success) {
                imageView.setColorFilter(HomeActivity.this.getResources().getColor(R.color.barrier_red_colorAccent), PorterDuff.Mode.SRC_IN);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setColorFilter(HomeActivity.this.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                    }
                }, 500);


            }
        }
    }

    private void callFingerDialog() {
        dialogFinger = new Dialog(HomeActivity.this);
        dialogFinger.setContentView(R.layout.default_fingerprint_layout);
        dialogFinger.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogFinger.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogFinger.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogFinger.setCancelable(false);
        dialogFinger.show();
        initializeAllFingerViews(dialogFinger);


    }

    private void initializeAllFingerViews(Dialog dialog) {

        tv_useMPin = dialog.findViewById(R.id.tv_UseMpin);
        view_line_finger = dialog.findViewById(R.id.view_line_finger);
        tv_useMPin.setOnClickListener(this);
        ivFingerAuth = dialog.findViewById(R.id.ivFingerAuth);
        checkFingerPrint();



    }

    private void callMpinDialog() {
        dialogMpin = new Dialog(HomeActivity.this);
        dialogMpin.setContentView(R.layout.dialog_default_mpin);
        dialogMpin.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogMpin.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogMpin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogMpin.setCancelable(false);
        dialogMpin.show();
        initializeAllMpinViews(dialogMpin);
    }

    private void initializeAllMpinViews(Dialog dialog) {

        tv_useFingerStats = dialog.findViewById(R.id.tv_useFingerprint);
        viewLine          = dialog.findViewById(R.id.view_line);
        tv_useFingerStats.setOnClickListener(this);

        if(!fingetStatus){
            tv_useFingerStats.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);

        }


        mFrameCloseKeyboard = dialog.findViewById(R.id.frameLayout_close_keyboard);

        mFrameNumber1 = dialog.findViewById(R.id.frameLayout_number1);
        mFrameNumber2 = dialog.findViewById(R.id.frameLayout_number2);
        mFrameNumber3 = dialog.findViewById(R.id.frameLayout_number3);
        mFrameNumber4 = dialog.findViewById(R.id.frameLayout_number4);
        mFrameNumber5 = dialog.findViewById(R.id.frameLayout_number5);
        mFrameNumber6 = dialog.findViewById(R.id.frameLayout_number6);
        mFrameNumber7 = dialog.findViewById(R.id.frameLayout_number7);
        mFrameNumber8 = dialog.findViewById(R.id.frameLayout_number8);
        mFrameNumber9 = dialog.findViewById(R.id.frameLayout_number9);
        mFrameNumber0 = dialog.findViewById(R.id.frameLayout_number0);
        mFrameNumberDeleteSpace = dialog.findViewById(R.id.frameLayout_deletePin);
        mFrameNumberNext = dialog.findViewById(R.id.frameLayout_next);
        //tvYourPIN        =  v.findViewById(R.id.textview_your_pin);

        mPin1 = dialog.findViewById(R.id.textView_pin1);
        mPin2 = dialog.findViewById(R.id.textView_pin2);
        mPin3 = dialog.findViewById(R.id.textView_pin3);
        mPin4 = dialog.findViewById(R.id.textView_pin4);

        overFlowMenuIcon = dialog.findViewById(R.id.overflowMenu);
        overFlowMenuIcon.setOnClickListener(this);

        forgetMpin = dialog.findViewById(R.id.ForgetMpin);
        resetMpin   = dialog.findViewById(R.id.ResetMpin);

        forgetMpin.setText(Html.fromHtml("<u>Forget</u>"));
        resetMpin.setText(Html.fromHtml("<u>Reset</u>"));

        forgetMpin.setOnClickListener(this);
        resetMpin.setOnClickListener(this);

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
                    Toast.makeText(HomeActivity.this, R.string.msg_complete_your_pin, Toast.LENGTH_LONG).show();
                    return;
                }
                //print your PIN
                String yourPin = "";
                for(int i=0; i<mListPin.size(); i++){
                    yourPin += mListPin.get(i);
                }
                //tvYourPIN.setText("Your PIN : " + yourPin);

                if(!yourPin.equals(userMpin)){
                    Toast.makeText(HomeActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogMpin.dismiss();
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
    public void onClick(View v) {
        Intent in = new Intent(HomeActivity.this , ForgetResetMpinActivity.class);

        switch (v.getId()){
            case R.id.tv_useFingerprint:
                callFingerDialog();
                dialogMpin.dismiss();

                break;

            case R.id.tv_UseMpin:
                callMpinDialog();
                dialogFinger.dismiss();
                break;

            case R.id.ForgetMpin:
                Constant.FORGET_RESET_MPIN = "FORGET";
                startActivity(in);
                break;

            case R.id.ResetMpin:
                Constant.FORGET_RESET_MPIN = "RESET";
                startActivity(in);
                break;

        }
    }
}
