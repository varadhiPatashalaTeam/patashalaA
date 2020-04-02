package in.varadhismartek.patashalaerp.DashboardModule.Syllabus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class SyllabusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.SYLLABUS_INBOX_FRAGMENT, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.SYLLABUS_INBOX_FRAGMENT:
                callFragment(new SyllabusInboxFragment(), Constant.SYLLABUS_INBOX_FRAGMENT, null, bundle);
                break;

            case Constant.SYLLABUS_VIEW_FRAGMENT:
                callFragment(new SyllabusViewFragment(), Constant.SYLLABUS_VIEW_FRAGMENT, null, bundle);
                break;

            case Constant.SYLLABUS_ADD_FRAGMENT:
                callFragment(new AddSyllabusFragment(), Constant.SYLLABUS_ADD_FRAGMENT, null, bundle);
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

            if (Constant.SYLLABUS_INBOX_FRAGMENT.equals(tag)){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            }
            else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(addBackStack)
                        .commit();
            }
        }
    }
}
