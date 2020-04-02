package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveBarrierFragment;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveSchoolBarrierFragment;
import in.varadhismartek.patashalaerp.R;

public class HomeWorkActivity extends AppCompatActivity {
    String BarrierType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);


        Bundle b = getIntent().getExtras();
        if (b!= null){
            BarrierType= b.getString("BARRIERS_TYPE");
        }

        switch (BarrierType){
            case "HomeWork_Barrier":

                loadFragment(Constant.HOMEWORK_BARRIER_FRAGMENT, null);
                break;

            case "Leave_Barrier":
                loadFragment(Constant.LEAVE_BARRIER_FRAGMENT, null);
                break;
        }


    }


    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {


            case Constant.HOMEWORK_BARRIER_FRAGMENT:
                callFragment(new Fragment_HomeworkBarriers(), Constant.HOMEWORK_BARRIER_FRAGMENT, null, null);
                break;

                case Constant.LEAVE_BARRIER_FRAGMENT:
                callFragment(new LeaveBarrierFragment(), Constant.LEAVE_BARRIER_FRAGMENT, null, null);
                break;

                case Constant.LEAVE_SCHOOL_BARRIER_FRAGMENT:
                callFragment(new LeaveSchoolBarrierFragment(), Constant.LEAVE_SCHOOL_BARRIER_FRAGMENT, null, null);
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

        }else{

            if (Constant.HOMEWORK_BARRIER_FRAGMENT.equals(tag)){
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
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
