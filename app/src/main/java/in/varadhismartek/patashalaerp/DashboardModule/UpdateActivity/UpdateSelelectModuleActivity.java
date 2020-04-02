package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.SelectModules.Screenshot;
import in.varadhismartek.patashalaerp.SelectModules.SelectModuleAdapter;
import in.varadhismartek.patashalaerp.SelectModules.SelectModuleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSelelectModuleActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_Selectmodules;
    private ArrayList<SelectModuleModel> modulesName;
    private SelectModuleAdapter selectModuleAdapterl;
    private Button btnSave_SelectedModule, btn_SelectAll;
    ImageView iv_back_btn, iv_downLoadModuleList;
    SharedPreferences pref;

    private ProgressDialog progressDialog = null;
    int spanCount;
    APIService mApiService;
    boolean module_status =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_module_list);
        pref = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        Constant.SCHOOL_ID = pref.getString("SchoolId", "0");


        Log.i("Constant.SCHOOL_ID***Ml", Constant.SCHOOL_ID);

        intializeViews();
        intializeListners();

        getSelectModule();
    }
    private void intializeListners() {
        // btnSave_SelectedModule.setOnClickListener(this);
        btn_SelectAll.setOnClickListener(this);
        iv_back_btn.setOnClickListener(this);
        iv_downLoadModuleList.setOnClickListener(this);
    }

    private void intializeViews() {
        mApiService = ApiUtils.getAPIService();
        btn_SelectAll = findViewById(R.id.btnSelectAll);
        rv_Selectmodules = findViewById(R.id.rv_SelectModules);
        btnSave_SelectedModule = findViewById(R.id.btnSaveSelectedModule);
        iv_back_btn = findViewById(R.id.iv_backBtn);
        iv_downLoadModuleList = findViewById(R.id.iv_downloadList);
        iv_back_btn.setBackground(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));

        iv_downLoadModuleList.setVisibility(View.GONE);
        modulesName = new ArrayList<>();


        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            spanCount = 4;
            Log.i("SCREEN_TYPE", "landscape");


        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
    }
    private void getSelectModule() {
        mApiService.getModuleList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try{
                    if (response.isSuccessful()){
                        Log.i("SELECTMODULE",""+response.body());
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                Log.d("module", key.toString());

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("moduledata", jsonObjectValue.toString());
                                String module_name = jsonObjectValue.getString("module_name");
                                module_status = jsonObjectValue.getBoolean("active_status");
                                modulesName.add(new SelectModuleModel(module_name, module_status));
                                addSelectedModules();


                            }
                        }

                    }else {
                        Log.i("SELECTMODULE","NO Data");
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SELECTMODULEERROR",""+t.toString());
            }
        });

    }
    private void addSelectedModules() {


        LinearLayoutManager linearLayoutManager = new GridLayoutManager(UpdateSelelectModuleActivity.this, spanCount);
        rv_Selectmodules.setLayoutManager(linearLayoutManager);
        rv_Selectmodules.setHasFixedSize(true);

        selectModuleAdapterl = new SelectModuleAdapter(modulesName, UpdateSelelectModuleActivity.this,
                btn_SelectAll, btnSave_SelectedModule, Constant.UPDATE);
        rv_Selectmodules.setAdapter(selectModuleAdapterl);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSaveSelectedModule:

                break;

            case R.id.iv_backBtn:
                Intent intent = new Intent(UpdateSelelectModuleActivity.this, DashboardSettingActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.iv_downloadList:
                shareScreen();
                break;


        }
    }
    private void shareScreen() {

        try {


            File cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "devdeeds");

            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }


            String path = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "devdeeds") + "/screenshot.jpg";

            Screenshot.savePic(Screenshot.takeScreenShot(this), path);

            Toast.makeText(UpdateSelelectModuleActivity.this, "Screenshot Saved", Toast.LENGTH_SHORT).show();


        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
    }
}
