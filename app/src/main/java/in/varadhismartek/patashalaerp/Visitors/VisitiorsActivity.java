package in.varadhismartek.patashalaerp.Visitors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class VisitiorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        loadFragment(Constant.FRAGMENT_VISITORS_INBOX, null);

    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.FRAGMENT_VISITORS_INBOX:
                callFragment(new VisitorsInboxFragment(), Constant.FRAGMENT_VISITORS_INBOX, null, bundle);
                break;

            case Constant.FRAGMENT_ADD_VISITIORS:
                callFragment(new AddVisitorsFragment(), Constant.FRAGMENT_ADD_VISITIORS, null, bundle);
                break;

            case Constant.FRAGMENT_VIEW_VISITIORS:
                callFragment(new ViewVisitorsFragment(), Constant.FRAGMENT_VIEW_VISITIORS, null, bundle);
                break;

            case Constant.FRAGMENT_VISITIORS_GEN_PASS:
                callFragment(new VisitorPassFragment(), Constant.FRAGMENT_VISITIORS_GEN_PASS, null, bundle);
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

            if (Constant.FRAGMENT_VISITORS_INBOX.equals(tag)){
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            }else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(addBackStack)
                        .commit();
            }
        }
    }
}
