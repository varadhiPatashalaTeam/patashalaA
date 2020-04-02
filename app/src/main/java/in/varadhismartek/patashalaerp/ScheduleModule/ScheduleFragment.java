package in.varadhismartek.patashalaerp.ScheduleModule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.varadhismartek.Utils.AppBarStateChangeListener;
import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.calenderView.CustomCalendarView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    CustomCalendarView customCalendar;
    RecyclerView recycler_view;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    AppBarLayout appbar;
    TextView tv_month;

    APIService mApiService;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        initViews(view);
        initListeners();


        collapsingToolbar.setContentScrimColor(Color.parseColor("#52b155"));
        tv_month.setText(Constant.TOOLBAR_MONTH);

        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());

                if (state.name().equals("COLLAPSED")){
                    toolbar.setVisibility(View.VISIBLE);
                    tv_month.setText(Constant.TOOLBAR_MONTH);

                }else {
                    toolbar.setVisibility(View.GONE);

                }
            }
        });

        customCalendar.onListPass(new CustomCalendarView.EventListPass() {
            @Override
            public void listPass(ArrayList<MyScheduleModel> arrayList) {

                setRecyclerView(arrayList);
                Log.d("MyScheduleModelSIZE", String.valueOf(arrayList.size()));
            }
        });



        return view;


    }


    private void initViews(View view) {

        appbar = view.findViewById(R.id.appbar);
        tv_month = view.findViewById(R.id.tv_month);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbar);
        toolbar = view.findViewById(R.id.toolbar);
        customCalendar = view.findViewById(R.id.customCalendar);
        recycler_view = view.findViewById(R.id.recycler_view);

        mApiService = ApiUtils.getAPIService();

    }

    private void initListeners() {

    }

    private void setRecyclerView(ArrayList<MyScheduleModel> list) {

        ScheduleAdapter adapter = new ScheduleAdapter(list,getActivity(), Constant.RV_LAYOUT_SCHEDULE_ROW_FRONT);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        recycler_view.setNestedScrollingEnabled(true);
        adapter.notifyDataSetChanged();
    }


}
