package in.varadhismartek.patashalaerp.DashboardModule.NoticeBoard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class NoticeBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.NOTICEBOARD_FRAGMENT, null);
    }

    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.NOTICEBOARD_FRAGMENT:
                callFragment(new NoticeBoardFragment(), Constant.NOTICEBOARD_FRAGMENT, null, bundle);
                break;

            case Constant.NOTICEBOARD_CREATE_FRAGMENT:
                callFragment(new NoticeBoardCreateFragment(), Constant.NOTICEBOARD_CREATE_FRAGMENT, null, bundle);
                break;

            case Constant.NOTICEBOARD_VIEW_FRAGMENT:
                callFragment(new NoticeBoardViewFragment(), Constant.NOTICEBOARD_VIEW_FRAGMENT, null, bundle);
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

            if (Constant.NOTICEBOARD_FRAGMENT.equals(tag)){
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
       // this.finish();
    }
}
