package in.varadhismartek.patashalaerp.DashboardModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_AddSubject;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_AssessmentGrade;
import in.varadhismartek.patashalaerp.R;


public class DashboardActivity extends AppCompatActivity {
    SharedPreferences pref = null, pref2;
    SharedPreferences.Editor editor;
    boolean booleanStatus = false;
    String MenuName="";
    ProgressDialog mProg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.frame_container);

        pref =getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        pref2 = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        editor =pref2.edit();
        editor.putString("ACTIVE_PAGE", "5");
        editor.apply();

        Constant.SCHOOL_ID = pref2.getString("SchoolId", "0");
        Constant.ACTIVE_PAGE = pref2.getString("ACTIVE_PAGE", "0");
        Constant.SCHOOL_ID = pref2.getString("SchoolId", "0");
        Constant.EMPLOYEE_BY_ID = pref2.getString("EmployeeId", "0");
        Constant.POC_NAME = pref2.getString("PocName", "0");
        Constant.POC_Mobile_Number = pref2.getString("PocMobileNo", "0");

        Constant.EMPLOYEE_PHOTO = pref2.getString("EmployeePhoto","" );
        Constant.EMPLOYEE_FNAME = pref2.getString("EmployeeFname","" );
        Constant. EMPLOYEE_LNAME= pref2.getString("EmployeeLname","" );
        Constant.EMPLOYEE_ADDHAR = pref2.getString("EmployeeAdhar","" );
        Constant.EMPLOYEE_EMP_CUSTOM_ID = pref2.getString("EmployeeEmpCusID","" );
        Constant.EMPLOYEE_DEPARTMENT = pref2.getString("EmployeeDept","" );
        Constant.EMPLOYEE_ROLE = pref2.getString("EmployeeRole","" );
        Constant.EMPLOYEE_WING = pref2.getString("EmployeeWing","" );




        Log.d("DefaultSOption", " "+Constant.SCHOOL_ID );




        Constant.SCHOOL_LOGO = pref2.getString("SchoolLogo", "0");

        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new Fragment_Dashboard()).addToBackStack(null).commit();

      /*  mProg = new ProgressDialog(this);
        mProg.show();
        mProg.setMessage("Loading");
        mProg.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mProg.dismiss();
            }
        },3000);*/




    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DashboardActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
