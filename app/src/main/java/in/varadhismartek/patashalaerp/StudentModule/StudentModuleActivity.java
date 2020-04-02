package in.varadhismartek.patashalaerp.StudentModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.Fragment_Employee_Attendance;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveViewFragment;
import in.varadhismartek.patashalaerp.R;

public class StudentModuleActivity extends AppCompatActivity {
    String fragmentType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            fragmentType = b.getString("Fragment_Type");

        }
        switch (fragmentType) {
            case "StudentList":
                //loadFragment(Constant.STUDENT_DETAILS, null);
                loadFragment(Constant.ALL_STUDENT_LIST, null);
                break;


            case "Student_details":
                loadFragment(Constant.STUDENT_DETAILS, null);
                break;

            case "Student_Attendance":
                loadFragment(Constant.STUDENT_ATTENDANCE_LIST, null);
                break;

            default:
                loadFragment(Constant.STU_CREATE_HOMEWORK, null);
        }


    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.ALL_STUDENT_LIST:
                callFragment(new StudentList_Fragment(), Constant.ALL_STUDENT_LIST, null, null);
                break;

            case Constant.STUDENT_DETAILS:
                callFragment(new StudentDetails_Fragment(), Constant.STUDENT_DETAILS, null, bundle);
                break;


            case Constant.STU_HOMEWORK_LIST:
                callFragment(new StudentHomeWork_Fragment(), Constant.STU_HOMEWORK_LIST, null, bundle);

                break;
            case Constant.STUDENT_HOMEWORK_VIEW:
                callFragment(new StudentViewHomeWork(), Constant.STUDENT_HOMEWORK_VIEW, null, bundle);

//                callFragment(new StudentViewHomeWork(), Constant.STU_HOMEWORK_LIST, null, bundle);

                //callFragment(new StudentHomeWork_Fragment(), Constant.STU_CREATE_HOMEWORK, null, bundle);
                break;

            case Constant.STU_CREATE_HOMEWORK:
                callFragment(new StudentHomeWork_Fragment(), Constant.STU_CREATE_HOMEWORK, null, bundle);
                break;


            case Constant.ATTENDANCE_FRAGMENT:
                callFragment(new Fragment_Employee_Attendance(), Constant.ATTENDANCE_FRAGMENT, null, bundle);
                break;

            case Constant.STUDENT_LEAVE:
                callFragment(new Fragment_Student_Leave(), Constant.STUDENT_LEAVE, null, bundle);
                break;

            case Constant.LEAVE_VIEW_FRAGMENT:
                callFragment(new LeaveViewFragment(), Constant.LEAVE_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.STUDENT_ATTENDANCE_LIST:
                callFragment(new Fragment_StudentAttendance_List(), Constant.STUDENT_ATTENDANCE_LIST, null, bundle);
                break;

                case Constant.ADD_STUDENT_ATTENDANCE:
                callFragment(new Fragment_AddStudentAttendance(), Constant.ADD_STUDENT_ATTENDANCE, null, bundle);
                break;

                case Constant.FEES_INBOX_FRAGMENT:
                callFragment(new FeesInboxFragment(), Constant.FEES_INBOX_FRAGMENT, null, bundle);
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

            if (Constant.STU_CREATE_HOMEWORK.equals(tag)) {
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

    }
}
