package in.varadhismartek.patashalaerp.AddDepartment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import in.varadhismartek.patashalaerp.R;

public class AddDepartmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_departmet);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new AddDepartmentFragment()).commit();
    }

  /*  public SelectedFragment loadSelectedFragment(ArrayList<String> arrayList, ArrayList<String> list) {

        Bundle bundle=new Bundle();
        bundle.putSerializable("arraylist",arrayList);
        bundle.putSerializable("dataArrayList",list);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        SelectedFragment selectedFragment=new SelectedFragment();
        selectedFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container,selectedFragment,"SelectedFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return selectedFragment;

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
