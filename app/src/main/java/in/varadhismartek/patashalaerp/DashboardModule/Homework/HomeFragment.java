package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;


public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button bt_homework_barrier  = view.findViewById(R.id.bt_homework_barrier);
        Button bt_homework_inbox  = view.findViewById(R.id.bt_homework_inbox);
        Button bt_homework_create  = view.findViewById(R.id.bt_homework_create);

        final HomeWorkActivity homeWorkActivity = (HomeWorkActivity) getActivity();

        bt_homework_barrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeWorkActivity.loadFragment(Constant.HOMEWORK_BARRIER_FRAGMENT, null);
            }
        });

        bt_homework_inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeWorkActivity.loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);

            }
        });

        bt_homework_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeWorkActivity.loadFragment(Constant.HOMEWORK_CREATE_FRAGMENT, null);

            }
        });


        return view;
    }







}
