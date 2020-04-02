package in.varadhismartek.patashalaerp.DashboardModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.Fragment_EmployeeView;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.Fragment_Employee_Attendance;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.Fragment_Employee_Leave;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveViewFragment;
import in.varadhismartek.patashalaerp.R;

public class AttendanceList extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.EMPLOYEE_LIST_ATTENDANCE, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {
        switch (fragmentString) {

            case Constant.EMPLOYEE_LIST_ATTENDANCE:
                callFragment(new Fragment_Emp_Attandance(), Constant.EMPLOYEE_LIST_ATTENDANCE, null, null);
                break;


            case Constant.ATTENDANCE_FRAGMENT:
                callFragment(new Fragment_Employee_Attendance(), Constant.ATTENDANCE_FRAGMENT, null, bundle);
                break;


                 /* case Constant.EMPLOYEE_VIEW_FRAGMENT:
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
*/

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
        // Intent intent = new Intent(EmployeeActivity.this, DashboardActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
        // finish();
    }
}
