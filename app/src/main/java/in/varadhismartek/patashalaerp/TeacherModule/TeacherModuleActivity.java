package in.varadhismartek.patashalaerp.TeacherModule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;

public class TeacherModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_container);

        loadFragment(Constant.TEACHER_HOMEWORK_LIST, null);
    }
    public void loadFragment(String fragmentString, Bundle bundle) {

        switch (fragmentString) {

            case Constant.TEACHER_HOMEWORK_LIST:
                callFragment(new Teacher_HomeWorkListFragment(), Constant.TEACHER_HOMEWORK_LIST, null, null);
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
            fragment.getArguments();

        } else {

            if (Constant.TEACHER_HOMEWORK_LIST.equals(tag)) {
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
    }
}
