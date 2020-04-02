package in.varadhismartek.patashalaerp.DashboardModule.NotificationModule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.NOTIFICATION_INBOX_FRAGMENT, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {
        switch (fragmentString) {


            case Constant.NOTIFICATION_INBOX_FRAGMENT:
                callFragment(new NotificationInboxFragment(), Constant.NOTIFICATION_INBOX_FRAGMENT, null, bundle);
                break;

            case Constant.NOTIFICATION_VIEW_FRAGMENT:
                callFragment(new ViewNotificationFragment(), Constant.NOTIFICATION_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.NOTIFICATION_CREATE_FRAGMENT:
                callFragment(new CreateNotificationFragment(), Constant.NOTIFICATION_CREATE_FRAGMENT, null, bundle);
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

            if (Constant.NOTIFICATION_INBOX_FRAGMENT.equals(tag)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        //.addToBackStack(null)
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
