package in.varadhismartek.patashalaerp.SelectModules;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv_Selectmodules;
    private ArrayList<SelectModuleModel> modulesName;
    private SelectModuleAdapter selectModuleAdapterl;
    private Button btnSave_SelectedModule, btn_SelectAll;
    ImageView iv_back_btn, iv_downLoadModuleList;
    SharedPreferences pref;

    private ProgressDialog progressDialog = null;
    int spanCount;
    APIService mApiService;
    boolean module_status = false;

    public ModuleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_module_list, container, false);
        pref = getActivity().getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);


        Constant.SCHOOL_ID = pref.getString("SchoolId", "0");
        Constant.EMPLOYEE_BY_ID = pref.getString("EmployeeId", "0");
        Constant.POC_NAME = pref.getString("PocName", "0");
        Constant.POC_Mobile_Number = pref.getString("PocMobileNo", "0");

        Log.i("Constant.SCHOOL_ID***Ml", Constant.SCHOOL_ID);
        Log.i("Constant.SCHOOL_ID***Ml", Constant.EMPLOYEE_BY_ID);

        intializeViews(v);
        intializeListners();

        setHasOptionsMenu(true);
        getSelectModule();


        return v;
    }

    private void getSelectModule() {
        mApiService.getModuleList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.i("SELECTMODULE", "" + response.body());
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = object.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("moduledata", jsonObjectValue.toString());
                                String module_name = jsonObjectValue.getString("module_name");
                                module_status = jsonObjectValue.getBoolean("active_status");

                                modulesName.add(new SelectModuleModel(module_name, module_status));
                                // addSelectedModules();


                            }
                        }

                    } else {
   /* **************************************set module from local******************************/


                        modulesName.clear();
                        String arr[] = getActivity().getResources().getStringArray(R.array.ModuleNames);
                        for (String anArr : arr) {
                            modulesName.add(new SelectModuleModel(anArr, false));
                        }
                        //addSelectedModules();
                        Log.i("SELECTMODULE", "NO Data");
                    }

                    addSelectedModules();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SELECTMODULEERROR", "" + t.toString());
            }
        });

    }

    private void addSelectedModules() {


        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), spanCount);
        rv_Selectmodules.setLayoutManager(linearLayoutManager);
        rv_Selectmodules.setHasFixedSize(true);

        selectModuleAdapterl = new SelectModuleAdapter(modulesName, getActivity(), btn_SelectAll,
                btnSave_SelectedModule, Constant._NOT_UPDATE);
        rv_Selectmodules.setAdapter(selectModuleAdapterl);


    }

    private void intializeListners() {
        btnSave_SelectedModule.setOnClickListener(this);
        btn_SelectAll.setOnClickListener(this);
        iv_back_btn.setOnClickListener(this);
        iv_downLoadModuleList.setOnClickListener(this);
    }


    private void intializeViews(View v) {
        mApiService = ApiUtils.getAPIService();
        btn_SelectAll = v.findViewById(R.id.btnSelectAll);
        rv_Selectmodules = v.findViewById(R.id.rv_SelectModules);
        btnSave_SelectedModule = v.findViewById(R.id.btnSaveSelectedModule);
        iv_back_btn = v.findViewById(R.id.iv_backBtn);
        iv_downLoadModuleList = v.findViewById(R.id.iv_downloadList);
        modulesName = new ArrayList<>();

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            spanCount = 4;
            Log.i("SCREEN_TYPE", "landscape");


        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSaveSelectedModule:

                break;

            case R.id.iv_backBtn:

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

            Screenshot.savePic(Screenshot.takeScreenShot(getActivity()), path);

            Toast.makeText(getActivity(), "Screenshot Saved", Toast.LENGTH_SHORT).show();


        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
    }


}
