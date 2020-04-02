package in.varadhismartek.patashalaerp.DashboardModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_AddSubject;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_QuestionBank;
import in.varadhismartek.patashalaerp.DashboardModule.Assessment.Fragment_QuestionBankList;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.Fragment_Employee_Attendance;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.Fragment_HomeworkCreate;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.Fragment_HomeworkInbox;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.Fragment_Homework_ReportList;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkViewFragment;
import in.varadhismartek.patashalaerp.DashboardModule.House.Fragment_HouseBarrier;
import in.varadhismartek.patashalaerp.DashboardModule.House.Fragment_Houses;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.AdminLeaveListFragment;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.Fragment_AllStudent_Leave;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveApplyFragment;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveDashboardFragment;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveViewFragment;
import in.varadhismartek.patashalaerp.R;

public class DashBoardMenuActivity extends AppCompatActivity {
    String fragmentType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frame_container);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragmentType = bundle.getString("Fragment_Type");
        }

        Log.i("fragmentType**", "" + fragmentType);

        switch (fragmentType) {


            case "School_Profile":
                loadFragment(Constant.SCHOOL_PROFILE, null);
                break;

            case "Subject":
                loadFragment(Constant.ADD_SUBJECT, null);
                break;

            case "HomeWorkInbox":
                loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);
                break;


            case "LeaveDashBoard":
                loadFragment(Constant.LEAVE_DASHBOARD_FRAGMENT, null);

                break;
            case "AttendanceMain":
                loadFragment(Constant.ATTENDANCE_DASHBOARD, null);
                break;
            case "Staff Attendance":
                loadFragment(Constant.ATTENDANCE_EMPLOYEE_LIST, null);
                break;


            case "QUESTIONBANK_LIST":
                loadFragment(Constant.QUESTIONBANK_LIST, null);
                break;

            case "House":
                loadFragment(Constant.ADD_HOUSES, null);
                break;


        }
    }

    public void loadFragment(String fragmentString, Bundle bundle) {
        switch (fragmentString) {

            case Constant.ADD_SUBJECT:
                callFragment(new Fragment_AddSubject(), Constant.ADD_SUBJECT, null, null);
                break;

            case Constant.SCHOOL_PROFILE:
                callFragment(new Fragment_SchoolProfile(), Constant.SCHOOL_PROFILE, null, null);
                break;


/***************************************Homework**********************************/

            case Constant.HOMEWORK_INBOX_FRAGMENT:
                callFragment(new Fragment_HomeworkInbox(), Constant.HOMEWORK_INBOX_FRAGMENT, null, null);
                break;

            case Constant.HOMEWORK_CREATE_FRAGMENT:
                callFragment(new Fragment_HomeworkCreate(), Constant.HOMEWORK_CREATE_FRAGMENT, null, null);
                break;

            case Constant.HOMEWORK_VIEW_FRAGMENT:
                callFragment(new HomeworkViewFragment(), Constant.HOMEWORK_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.HOMEWORK_REPORT_LIST:
                callFragment(new Fragment_Homework_ReportList(), Constant.HOMEWORK_REPORT_LIST, null, bundle);
                break;


            /*************************************Leave Dashboard**********************************/

            case Constant.LEAVE_DASHBOARD_FRAGMENT:
                callFragment(new LeaveDashboardFragment(), Constant.LEAVE_DASHBOARD_FRAGMENT, null, null);
                break;

            case Constant.LEAVE_ADMIN_LIST_FRAGMENT:
                callFragment(new AdminLeaveListFragment(), Constant.LEAVE_ADMIN_LIST_FRAGMENT, null, null);
                break;

            case Constant.LEAVE_VIEW_FRAGMENT:
                callFragment(new LeaveViewFragment(), Constant.LEAVE_VIEW_FRAGMENT, null, bundle);
                break;
            case Constant.LEAVE_APPLY_FRAGMENT:
                callFragment(new LeaveApplyFragment(), Constant.LEAVE_APPLY_FRAGMENT, null, bundle);
                break;

            case Constant.ALL_STUDENT_LEAVE:
                callFragment(new Fragment_AllStudent_Leave(), Constant.ALL_STUDENT_LEAVE, null, null);
                break;


            /**********************AttendanceMain******************************/

            case Constant.ATTENDANCE_DASHBOARD:
                callFragment(new Fragment_EmployeeAttendance(), Constant.ATTENDANCE_DASHBOARD, null, null);
                break;


            case Constant.ATTENDANCE_EMPLOYEE_LIST:
                //callFragment(new Attendance_EmployeeList(), Constant.ATTENDANCE_EMPLOYEE_LIST, null, null);
                break;

            case Constant.ATTENDANCE_FRAGMENT:
                callFragment(new Fragment_Employee_Attendance(), Constant.ATTENDANCE_FRAGMENT, null, bundle);
                break;


            /**********************question bank******************************/
            case Constant.QUESTIONBANK_LIST:
                callFragment(new Fragment_QuestionBankList(), Constant.QUESTIONBANK_LIST, null, null);
                break;


            case Constant.QUESTIONBANK__CREATE:
                callFragment(new Fragment_QuestionBank(), Constant.QUESTIONBANK__CREATE, null, null);
                break;


            case Constant.ADD_HOUSES:
                callFragment(new Fragment_HouseBarrier(), Constant.ADD_HOUSES, null, null);
                break;

            case Constant.ADD_HOUSES_LIST:
                callFragment(new Fragment_Houses(), Constant.ADD_HOUSES_LIST, null, bundle);

                break;
        }


    }


    private void callFragment(Fragment fragment, String tag, String addBackStack, Bundle bundle) {

        if (bundle != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(addBackStack)
                    .commit();
            fragment.setArguments(bundle);

        } else {

            if (Constant.ADD_SUBJECT.equals(tag)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(null)
                        .commit();
            }

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(addBackStack)
                    .commit();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         this.finish();
        /*Intent intent = new Intent(DashBoardMenuActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();*/
    }
}
