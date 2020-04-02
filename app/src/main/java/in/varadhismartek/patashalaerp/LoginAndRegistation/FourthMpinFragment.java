package in.varadhismartek.patashalaerp.LoginAndRegistation;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.PasscodeView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.SelectModules.SelectModuleLandingActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourthMpinFragment extends Fragment implements View.OnClickListener {

    PasscodeView                 passcode_View;

    private Dialog               dialog = null;

    boolean                      fingerAuthenticateStatus = true;

    ImageView                    iv_fingerAuthImage, iv_mPinAuthImage;

    String                       default_authenticate;

    SharedPreferences            defaultStatusForSecurity = null,preferencesLogin;

    Button                       btnMpin,btnFingerPrint;

    private String               userMpin;

    public FourthMpinFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fourth_mpin, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        defaultStatusForSecurity = getActivity().getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
      //  preferencesLogin = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);

        Log.d("empId**ID3", "" +  Constant.EMPLOYEE_BY_ID+Constant.POC_Mobile_Number+Constant.SCHOOL_LOGO);

        Log.i("str4444", "" + Constant.SCHOOL_ID);
        Log.i("str4444", "" + Constant.EMPLOYEE_BY_ID);
        Log.i("str4444", "" + Constant.SCHOOL_LOGO);
        Log.i("str4444", "" + Constant.POC_NAME);
        Log.i("str4444", "" + Constant.poc_mob);
        Log.i("str4444", "" + Constant.POC_Mobile_Number);
        Log.i("ename1**", "" + Constant.EMPLOYEE_FNAME);
        Log.i("str4444", Constant.EMPLOYEE_LNAME);
        Log.i("str4444", Constant.EMPLOYEE_ADDHAR);
        Log.i("str4444", Constant.EMPLOYEE_ROLE);
        Log.i("str4444", Constant.EMPLOYEE_DEPARTMENT);
        Log.i("str4444", Constant.EMPLOYEE_WING);

        initializePassCodeView(v);
        return v;
    }

    private void initializePassCodeView(View v) {

        passcode_View = v.findViewById(R.id.passcodeView);
        passcode_View.setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(String number) {
                userMpin = number;
                Log.d("MPINUSER", ""+number);
                checkFingerAuthenticationIsThere();

            }
        });
    }

    private void checkFingerAuthenticationIsThere() {

        SharedPreferences.Editor edt = defaultStatusForSecurity.edit();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager)getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
            assert fingerprintManager != null;
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                fingerAuthenticateStatus = false;
                openDialogForDefaultSecurity();

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                fingerAuthenticateStatus = false;
                openDialogForDefaultSecurity();

            } else {
                // Everything is ready for fingerprint authentication
                fingerAuthenticateStatus = true;
                openDialogForDefaultSecurity();
            }

            edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
            edt.apply();
        }
        else {
            edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
            edt.apply();
        }

    }

    private void openDialogForDefaultSecurity() {

        if(fingerAuthenticateStatus) {

            assert getActivity()!=null;
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.choose_default_authenticate_a);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            iv_fingerAuthImage = dialog.findViewById(R.id.fingerAuth);
            iv_mPinAuthImage   = dialog.findViewById(R.id.mPinAuth);
            btnFingerPrint     = dialog.findViewById(R.id.btn_fingerPrint);
            btnMpin            = dialog.findViewById(R.id.btn_mpin);
            btnMpin.setOnClickListener(this);
            btnFingerPrint.setOnClickListener(this);
            iv_fingerAuthImage.setOnClickListener(this);
            iv_mPinAuthImage.setOnClickListener(this);
            dialog.setCancelable(false);
            dialog.show();
        }
        else {

            assert getActivity()!=null;
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.choose_default_authenticate_a);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout linearLayoutFingerAuth = dialog.findViewById(R.id.finger_ll);
            View viewVertical = dialog.findViewById(R.id.viewVertical);
            viewVertical.setVisibility(View.GONE);
            LinearLayout linearLayoutFingerbtn = dialog.findViewById(R.id.fingerPrintBtnLayout);
            linearLayoutFingerbtn.setVisibility(View.GONE);
            linearLayoutFingerAuth.setVisibility(View.GONE);
            iv_fingerAuthImage = dialog.findViewById(R.id.fingerAuth);
            iv_mPinAuthImage   = dialog.findViewById(R.id.mPinAuth);
            btnMpin            = dialog.findViewById(R.id.btn_mpin);
            btnMpin.setOnClickListener(this);
            iv_fingerAuthImage.setOnClickListener(this);
            iv_mPinAuthImage.setOnClickListener(this);
            dialog.setCancelable(false);
            dialog.show();


        }

    }

    @Override
    public void onClick(View v) {

        Intent in = new Intent(getActivity(), SelectModuleLandingActivity.class);
        assert getActivity()!=null;
        SharedPreferences.Editor edt = defaultStatusForSecurity.edit();

        switch (v.getId()){

            case R.id.mPinAuth:

                iv_mPinAuthImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.check_animation));
                iv_mPinAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_checked));
                iv_fingerAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_fingerprint_grey));
                default_authenticate = "MPIN";
                edt.putString("DefaultSOption", default_authenticate);
                edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
                edt.putString("MPINUSER", userMpin);
                edt.putString("Audience", Constant.audience_type);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("EmployeeId", Constant.EMPLOYEE_BY_ID);
                edt.putString("PocName", Constant.POC_NAME);
                edt.putString("PocMobileNo", Constant.POC_Mobile_Number);
                //edt.putString("PocMobileNo", Constant.poc_mob);
                edt.putString("ManagementID", Constant.MANAGEMENT_ID);
                edt.putString("SchoolLogo",  Constant.SCHOOL_LOGO);
                edt.putString("EmployeePhoto",  Constant.EMPLOYEE_PHOTO);
                edt.putString("EmployeeFname",  Constant.EMPLOYEE_FNAME);
                edt.putString("EmployeeLname",  Constant.EMPLOYEE_LNAME);
                edt.putString("EmployeeAdhar",  Constant.EMPLOYEE_ADDHAR);
                edt.putString("EmployeeEmpCusID",  Constant.EMPLOYEE_EMP_CUSTOM_ID);
                edt.putString("EmployeeDept",  Constant.EMPLOYEE_DEPARTMENT);
                edt.putString("EmployeeRole",  Constant.EMPLOYEE_ROLE);
                edt.putString("EmployeeWing",  Constant.EMPLOYEE_WING);
                edt.putBoolean("activity_executed", true);


                edt.apply();
                dialog.dismiss();
                startActivity(in);
                getActivity().finish();

                break;

            case R.id.fingerAuth:
                iv_fingerAuthImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.check_animation));
                iv_fingerAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_checked));
                iv_mPinAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_lock_a));
                default_authenticate = "FINGER";
                edt.putString("DefaultSOption", default_authenticate);
                edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
                edt.putString("MPINUSER", userMpin);
                edt.putString("Audience", Constant.audience_type);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("EmployeeId", Constant.EMPLOYEE_BY_ID);
                edt.putString("PocName", Constant.POC_NAME);
                //edt.putString("PocMobileNo", Constant.poc_mob);
                edt.putString("PocMobileNo", Constant.POC_Mobile_Number);
                edt.putString("ManagementID", Constant.MANAGEMENT_ID);
                edt.putString("SchoolLogo",  Constant.SCHOOL_LOGO);
                edt.putString("EmployeePhoto",  Constant.EMPLOYEE_PHOTO);
                edt.putString("EmployeeFname",  Constant.EMPLOYEE_FNAME);
                edt.putString("EmployeeLname",  Constant.EMPLOYEE_LNAME);
                edt.putString("EmployeeAdhar",  Constant.EMPLOYEE_ADDHAR);
                edt.putString("EmployeeEmpCusID",  Constant.EMPLOYEE_EMP_CUSTOM_ID);
                edt.putString("EmployeeDept",  Constant.EMPLOYEE_DEPARTMENT);
                edt.putString("EmployeeRole",  Constant.EMPLOYEE_ROLE);
                edt.putString("EmployeeWing",  Constant.EMPLOYEE_WING);
                edt.putBoolean("activity_executed", true);

                edt.apply();
                dialog.dismiss();
                startActivity(in);
                getActivity().finish();

                break;
            case R.id.btn_mpin:
                iv_mPinAuthImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.check_animation));
                iv_mPinAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_checked));
                iv_fingerAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_fingerprint_grey));
                default_authenticate = "MPIN";
                edt.putString("DefaultSOption", default_authenticate);
                edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
                edt.putString("MPINUSER", userMpin);
                edt.putString("Audience", Constant.audience_type);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("EmployeeId", Constant.EMPLOYEE_BY_ID);
                edt.putString("PocName", Constant.POC_NAME);
                //edt.putString("PocMobileNo", Constant.poc_mob);
                edt.putString("PocMobileNo", Constant.POC_Mobile_Number);
                edt.putString("ManagementID", Constant.MANAGEMENT_ID);
                edt.putBoolean("activity_executed", true);
                edt.putString("SchoolLogo",  Constant.SCHOOL_LOGO);
                edt.putString("EmployeePhoto",  Constant.EMPLOYEE_PHOTO);
                edt.putString("EmployeeFname",  Constant.EMPLOYEE_FNAME);
                edt.putString("EmployeeLname",  Constant.EMPLOYEE_LNAME);
                edt.putString("EmployeeAdhar",  Constant.EMPLOYEE_ADDHAR);
                edt.putString("EmployeeEmpCusID",  Constant.EMPLOYEE_EMP_CUSTOM_ID);
                edt.putString("EmployeeDept",  Constant.EMPLOYEE_DEPARTMENT);
                edt.putString("EmployeeRole",  Constant.EMPLOYEE_ROLE);
                edt.putString("EmployeeWing",  Constant.EMPLOYEE_WING);

                edt.apply();
                dialog.dismiss();
                startActivity(in);
                getActivity().finish();

                break;

            case R.id.btn_fingerPrint:
                iv_fingerAuthImage.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.check_animation));
                iv_fingerAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_checked));
                iv_mPinAuthImage.setBackground(getResources().getDrawable(R.drawable.ic_lock_a));
                default_authenticate = "FINGER";
                edt.putString("DefaultSOption", default_authenticate);
                edt.putBoolean("FingerPrintStatus", fingerAuthenticateStatus);
                edt.putString("MPINUSER", userMpin);
                edt.putString("Audience", Constant.audience_type);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("SchoolId", Constant.SCHOOL_ID);
                edt.putString("EmployeeId", Constant.EMPLOYEE_BY_ID);
                edt.putString("PocName", Constant.POC_NAME);
                //edt.putString("PocMobileNo", Constant.poc_mob);
                edt.putString("PocMobileNo", Constant.POC_Mobile_Number);
                edt.putString("ManagementID", Constant.MANAGEMENT_ID);
                edt.putString("SchoolLogo",  Constant.SCHOOL_LOGO);

                edt.putString("EmployeePhoto",  Constant.EMPLOYEE_PHOTO);
                edt.putString("EmployeeFname",  Constant.EMPLOYEE_FNAME);
                edt.putString("EmployeeLname",  Constant.EMPLOYEE_LNAME);
                edt.putString("EmployeeAdhar",  Constant.EMPLOYEE_ADDHAR);
                edt.putString("EmployeeEmpCusID",  Constant.EMPLOYEE_EMP_CUSTOM_ID);
                edt.putString("EmployeeDept",  Constant.EMPLOYEE_DEPARTMENT);
                edt.putString("EmployeeRole",  Constant.EMPLOYEE_ROLE);
                edt.putString("EmployeeWing",  Constant.EMPLOYEE_WING);
                edt.putBoolean("activity_executed", true);

                edt.apply();
                dialog.dismiss();
                startActivity(in);
                getActivity().finish();
                break;


        }


    }

}
