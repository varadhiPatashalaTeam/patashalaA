package in.varadhismartek.patashalaerp.LoginAndRegistation;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstChooseAudienceFragment extends Fragment implements View.OnClickListener {

    private LinearLayout ll_parent, ll_teacher, ll_management;
    SharedPreferences pref, pref2;
    SharedPreferences.Editor editor;
    boolean booleanStatus = false;

    public FirstChooseAudienceFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_choose_audience, container, false);

        pref = getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        Constant.SCHOOL_ID = pref.getString("SchoolId", "0");
        Log.i("Constant.SCHOOL_ID***FC", Constant.SCHOOL_ID);

        pref2 = getActivity().getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        booleanStatus = pref2.getBoolean("activity_executed", false);
        Constant.SCHOOL_LOGO = pref2.getString("SchoolLogo","" );

        Constant.EMPLOYEE_PHOTO = pref2.getString("EmployeePhoto","" );
        Constant.EMPLOYEE_FNAME = pref2.getString("EmployeeFname","" );
        Constant. EMPLOYEE_LNAME= pref2.getString("EmployeeLname","" );
        Constant.EMPLOYEE_ADDHAR = pref2.getString("EmployeeAdhar","" );
        Constant.EMPLOYEE_EMP_CUSTOM_ID = pref2.getString("EmployeeEmpCusID","" );
        Constant.EMPLOYEE_DEPARTMENT = pref2.getString("EmployeeDept","" );
        Constant.EMPLOYEE_ROLE = pref2.getString("EmployeeRole","" );
        Constant.EMPLOYEE_WING = pref2.getString("EmployeeWing","" );


        Log.i("booleanStatus_FC", "" + booleanStatus);




        if (booleanStatus) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {

            initViews(v);
            initOnClickListner();

        }

        return v;

    }

    private void initOnClickListner() {
        ll_management.setOnClickListener(this);
        ll_teacher.setOnClickListener(this);
        ll_parent.setOnClickListener(this);
    }

    private void initViews(View v) {
        ll_parent = v.findViewById(R.id.linearLayoutParent);
        ll_teacher = v.findViewById(R.id.linearLayoutTeacher);
        ll_management = v.findViewById(R.id.linearLayoutManagement);
    }

    @Override
    public void onClick(View v) {
        LoginScreenActivity loginScreenActivity = (LoginScreenActivity) getActivity();
        assert loginScreenActivity != null;

        switch (v.getId()) {

            case R.id.linearLayoutManagement:
                Constant.audience_type = getResources().getString(R.string.management);
                loginScreenActivity.loadFragment(Constant.SECOND_SEARCH_USER_FRAGMENT, null);
                break;

            case R.id.linearLayoutTeacher:
                Constant.audience_type = getResources().getString(R.string.staff);
                loginScreenActivity.loadFragment(Constant.SECOND_SEARCH_USER_FRAGMENT, null);
                break;

            case R.id.linearLayoutParent:
                Constant.audience_type = getResources().getString(R.string.parent);
                loginScreenActivity.loadFragment(Constant.SECOND_SEARCH_USER_FRAGMENT, null);
                break;

        }
        editor = pref.edit();
        editor.putString("SELECT_TAB", "true");
        editor.putBoolean("ACTIVITY_STATUS", true);
        editor.commit();


    }


}
