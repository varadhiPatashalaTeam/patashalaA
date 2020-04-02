package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkViewFragment;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveViewFragment;
import in.varadhismartek.patashalaerp.R;

public class EmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        //loadFragment(Constant.EMPLOYEE_ACCOUNT_DETAILS, null);
        loadFragment(Constant.EMPLOYEE_LIST, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {
        switch (fragmentString) {

            case Constant.EMPLOYEE_LIST:
                callFragment(new Fragment_EmployeeList(), Constant.EMPLOYEE_LIST, null, null);
                break;
            case Constant.EMPLOYEE_VIEW_FRAGMENT:
                callFragment(new Fragment_EmployeeView(), Constant.EMPLOYEE_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.ATTENDANCE_FRAGMENT:
                callFragment(new Fragment_Employee_Attendance(), Constant.ATTENDANCE_FRAGMENT, null, bundle);
                break;

            case Constant.EMPLOYEE_LEAVE:
                callFragment(new Fragment_Employee_Leave(), Constant.EMPLOYEE_LEAVE, null, bundle);
                break;

            case Constant.LEAVE_VIEW_FRAGMENT:
                callFragment(new LeaveViewFragment(), Constant.LEAVE_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.EMPLOYEE_HOMEWORK:
                callFragment(new Fragment_Homework_Employee(), Constant.EMPLOYEE_HOMEWORK, null, bundle);
                break;

            case Constant.HOMEWORK_VIEW_FRAGMENT:
                callFragment(new HomeworkViewFragment(), Constant.HOMEWORK_VIEW_FRAGMENT, null, bundle);
                break;

                case Constant.EMPLOYEE_ACCOUNT_DETAILS:
                callFragment(new EmployeeAccountDetails(), Constant.EMPLOYEE_ACCOUNT_DETAILS, null, bundle);
                break;

                case Constant.EMPLOYEE_PAYSLIP:
                callFragment(new EmployeePaySlipBarrier(), Constant.EMPLOYEE_PAYSLIP, null, bundle);
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

            if (Constant.EMPLOYEE_LIST.equals(tag)) {
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
       // this.finish();


    }
}
