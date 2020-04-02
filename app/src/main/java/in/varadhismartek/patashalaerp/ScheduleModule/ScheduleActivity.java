package in.varadhismartek.patashalaerp.ScheduleModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.SCHEDULE_FRAGMENT, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.SCHEDULE_FRAGMENT:
                callFragment(new ScheduleFragment(), Constant.SCHEDULE_FRAGMENT, null, bundle);
                break;

            case Constant.SCHEDULE_DETAILS_FRAGMENT:
                callFragment(new ViewScheduleFragment(), Constant.SCHEDULE_DETAILS_FRAGMENT, null, bundle);
                break;

            case Constant.SCHEDULE_LIST_FRAGMENT:
                callFragment(new ScheduleListFragment(), Constant.SCHEDULE_LIST_FRAGMENT, null, bundle);
                break;

            case Constant.ADD_SCHEDULE_MANAGEMENT_FRAGMENT:
                callFragment(new AddScheduleManagementFragment(), Constant.ADD_SCHEDULE_MANAGEMENT_FRAGMENT, null, bundle);
                break;

         /*   case Constant.ADD_SCHEDULE_FRAGMENT:
                callFragment(new AddScheduleFragment(), Constant.ADD_SCHEDULE_FRAGMENT, null, bundle);
                break;





            case Constant.SCHEDULE_INBOX_FRAGMENT:
                callFragment(new ScheduleInboxFragment(), Constant.ADD_SCHEDULE_MANAGEMENT_FRAGMENT, null, bundle);
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

        }else{

            if (Constant.SCHEDULE_FRAGMENT.equals(tag)){
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
}
