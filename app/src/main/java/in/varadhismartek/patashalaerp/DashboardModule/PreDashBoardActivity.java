package in.varadhismartek.patashalaerp.DashboardModule;

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
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import in.varadhismartek.patashalaerp.LoginAndRegistation.HomeActivity;
import in.varadhismartek.patashalaerp.R;

public class PreDashBoardActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences  pref2= null;
    SharedPreferences.Editor editor;
    Dialog dialogMpin = null;
    Dialog dialogFinger = null;

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
    private static final String KEY_NAME = "AndroidExamples";
    private KeyStore keyStore;
    private Cipher cipher;
    String userMpin;


    private TextView tv_useMPin,tvUserName;
    private View view_line_finger;
    private ImageView ivFingerAuth;
    String employeeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_dash_board);

        pref2 = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        Log.d("DefaultSOptionA", " "+pref2.getString("DefaultSOption",""));

        employeeName = pref2.getString("EmployeeFname","")+" "+pref2.getString("EmployeeLname","");

        if(pref2.getString("DefaultSOption","").equalsIgnoreCase("MPIN")) {
            userMpin = pref2.getString("MPINUSER", "");
            callMpin();


        }
        else if(pref2.getString("DefaultSOption","").equalsIgnoreCase("FINGER")){

            callFinger();

        }

    }

    private void callFinger() {

        dialogFinger = new Dialog(PreDashBoardActivity.this);
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
        tvUserName = dialog.findViewById(R.id.tvUserName);
        tvUserName.setText("Welcome back "+employeeName);
        view_line_finger = dialog.findViewById(R.id.view_line_finger);
        tv_useMPin.setOnClickListener(this);
        ivFingerAuth = dialog.findViewById(R.id.ivFingerAuth);
        checkFingerPrint();
    }

    private void checkFingerPrint() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager)this.getSystemService(Context.FINGERPRINT_SERVICE);
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
                    FingerprintHandler helper = new FingerprintHandler(PreDashBoardActivity.this, ivFingerAuth);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }


            }
        } else {

            Log.d("Fingerhardware", "No");

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


    private void callMpin() {

        dialogMpin = new Dialog(PreDashBoardActivity.this);
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
        tvUserName = dialog.findViewById(R.id.tvUserName);

        tvUserName.setText("Welcome back "+employeeName);
        viewLine          = dialog.findViewById(R.id.view_line);
        tv_useFingerStats.setOnClickListener(this);


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
                    Toast.makeText(PreDashBoardActivity.this, R.string.msg_complete_your_pin, Toast.LENGTH_LONG).show();
                    return;
                }
                //print your PIN
                String yourPin = "";
                for(int i=0; i<mListPin.size(); i++){
                    yourPin += mListPin.get(i);
                }
                //tvYourPIN.setText("Your PIN : " + yourPin);

                if(!yourPin.equals(userMpin)){
                    Toast.makeText(PreDashBoardActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialogMpin.dismiss();
                    Intent intent = new Intent(PreDashBoardActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_useFingerprint:
                callFinger();
                dialogMpin.dismiss();

                break;

            case R.id.tv_UseMpin:
                callMpin();
                dialogFinger.dismiss();
                break;



        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //dialogMpin.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dialogMpin.dismiss();

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
            if (ActivityCompat.checkSelfPermission(PreDashBoardActivity.this, android.Manifest.permission.USE_FINGERPRINT)
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
                imageView.setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
                dialogFinger.dismiss();
                Intent intent = new Intent(PreDashBoardActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();


            }
            if (!success) {
                imageView.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
                    }
                }, 500);


            }
        }
    }
}
