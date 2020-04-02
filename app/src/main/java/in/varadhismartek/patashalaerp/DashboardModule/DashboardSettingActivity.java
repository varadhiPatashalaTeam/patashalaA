package in.varadhismartek.patashalaerp.DashboardModule;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.varadhismartek.Indicator.CirclePageIndicator;
import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter.GridViewAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.extraUtils.ModuleModel;
import in.varadhismartek.patashalaerp.DashboardModule.extraUtils.ViewPagerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.bottomnavigation.BottomNavigationThreeActivity;
import in.varadhismartek.patashalaerp.bottomnavigation.BottomNavigationTwoActivity;

public class DashboardSettingActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    public static int item_grid_num = 8;
    public static int number_columns = 2;
    String strImageUrl = "http://13.233.109.165:8000/media/";
    private ViewPager view_pager;
    private ViewPagerAdapter mAdapter;
    private List<ModuleModel> moduleList;
    private List<GridView> gridList = new ArrayList<>();
    private CirclePageIndicator indicator;
    ArrayList<String> name = new ArrayList<>();

    ImageView editSetting,iv_schoolLogo;
    APIService mApiService;
    AutoCompleteTextView edSearch;
    GridViewAdapter adapter;

    LinearLayout llFirstItem,llSecondItem,llThirdItem,llForthItem;

    ImageView ivBarriers,ivRefresh,ivLogout;
    TextView tvBarriers;
    View viewFour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_setting);

        mApiService = ApiUtils.getAPIService();
        initViews();
        initListners();

        initDatas();
    }
    private void initViews() {
        view_pager =  findViewById(R.id.view_pager);

        iv_schoolLogo = findViewById(R.id.iv_schoolLogo);

        ivRefresh = findViewById(R.id.ivRefresh);
        ivLogout = findViewById(R.id.iv_logout);

        ivRefresh.setVisibility(View.GONE);
        ivLogout.setVisibility(View.GONE);

        edSearch = findViewById(R.id.et_search);
        editSetting = findViewById(R.id.edit_setting);

        llFirstItem = findViewById(R.id.llFirstItem);
        llSecondItem = findViewById(R.id.llSecondItem);
        llThirdItem = findViewById(R.id.llThirdItem);
        llForthItem = findViewById(R.id.llForthItem);

        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        moduleList = new ArrayList<>();
        indicator =  findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(view_pager);

        editSetting.setVisibility(View.GONE);
        edSearch.addTextChangedListener(this);
        iv_schoolLogo.setBackground(getResources().getDrawable(R.drawable.image_backgroung_green));


        ivBarriers = findViewById(R.id.ivForthItem);
        tvBarriers = findViewById(R.id.tvForthItem);
        viewFour = findViewById(R.id.viewFour);
        ivBarriers.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting_filled));
        tvBarriers.setTextColor(getResources().getColor(R.color.orange_colorPrimaryDark));
        viewFour.setVisibility(View.VISIBLE);

        if (Constant.SCHOOL_LOGO.isEmpty() || Constant.SCHOOL_LOGO.equalsIgnoreCase("") || Constant.SCHOOL_LOGO.equals(null)) {

        } else {
            Picasso.with(DashboardSettingActivity.this).
                    load(strImageUrl + Constant.SCHOOL_LOGO)
                    .placeholder(R.drawable.patashala_logo)
                    //.resize(50, 50)
                    .into(iv_schoolLogo);

        }
    }


    private void initListners() {

        llFirstItem.setOnClickListener(this);
        llSecondItem.setOnClickListener(this);
        llThirdItem.setOnClickListener(this);
        llForthItem.setOnClickListener(this);

    }

    private void initDatas() {
        Log.i("nameSize**",""+name.size());
        if (moduleList.size() > 0) {
            moduleList.clear();
        }
        if (gridList.size() > 0) {
            gridList.clear();
        }




        moduleList.add(new ModuleModel (R.drawable.ic_assesment_green,"Module"));
        moduleList.add(new ModuleModel(R.drawable.ic_wings_green,"Wings"));
        moduleList.add(new ModuleModel(R.drawable.ic_timetable,"Department"));
        moduleList.add(new ModuleModel(R.drawable.ic_roles_green,"Roles"));
        moduleList.add(new ModuleModel(R.drawable.ic_staff_green,"Staff Barrier's"));
        moduleList.add(new ModuleModel(R.drawable.ic_maker_checker_green,"Maker & Checker"));
        moduleList.add(new ModuleModel(R.drawable.ic_div_green,"Division"));
        moduleList.add(new ModuleModel(R.drawable.ic_class_green,"Class"));
        moduleList.add(new ModuleModel(R.drawable.ic_section_green,"Section"));
        moduleList.add(new ModuleModel(R.drawable.ic_session_green,"Session"));
        moduleList.add(new ModuleModel(R.drawable.ic_homework_green,"Homework"));


        int pageSize = moduleList.size() % item_grid_num == 0
                ? moduleList.size() / item_grid_num
                : moduleList.size() / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(this);
            adapter = new GridViewAdapter(DashboardSettingActivity.this,moduleList, i);
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
        mAdapter.add(gridList);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.llFirstItem:

                Intent intentFirst = new Intent(this, DashboardActivity.class);
                intentFirst.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentFirst);

                break;

            case R.id.llSecondItem:
                Intent intentSecond = new Intent(this, BottomNavigationTwoActivity.class);
                intentSecond.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentSecond);
                break;

            case R.id.llThirdItem:
                Intent intentThreee = new Intent(this, BottomNavigationThreeActivity.class);
                intentThreee.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentThreee);
                break;


        }
    }
}
