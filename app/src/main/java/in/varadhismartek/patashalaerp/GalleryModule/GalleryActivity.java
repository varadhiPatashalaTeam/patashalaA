package in.varadhismartek.patashalaerp.GalleryModule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.GALLERY_FRAGMENT, null);

    }

    public void loadFragment(String fragmentTAG, Bundle bundle) {

        switch (fragmentTAG) {

            case Constant.GALLERY_FRAGMENT:
                callFragment(new GalleryFragment(), Constant.GALLERY_FRAGMENT, null, null);
                break;

            case Constant.GALLERY_GRID_FRAGMENT:
                callFragment(new GalleryGridFragment(), Constant.GALLERY_GRID_FRAGMENT, null, bundle);
                break;

            case Constant.GALLERY_VIEW_FRAGMENT:
                callFragment(new GalleryViewFragment(), Constant.GALLERY_VIEW_FRAGMENT, null, bundle);
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

            if (Constant.GALLERY_FRAGMENT.equals(tag)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment, tag)
                        .addToBackStack(addBackStack)
                        .commit();
            }
        }
    }
}