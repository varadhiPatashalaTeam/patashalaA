package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.AddRoles.RolesLandingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateRecyclerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDepartmentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_enter;
    ImageButton add_image;
    Button button_added;
    RecyclerView recycler_view;
    ArrayList<Data> list;
    ArrayList<String> checker;
    ArrayList<String> maker;
    UpdateRecyclerAdapter recyclerAdapter;
    ArrayList<String> checkedArrayList;
    ArrayList<String> unCheckedArrayList;
    Spinner wingsSpinner;
    ImageView ivAddDept, ivBack, iv_NO_Records;
    APIService mApiService;
    Utilities utilities;


    ArrayList<String> arrayListWings;


    private ProgressDialog progressDialog = null;
    private String wingsValue;

    boolean wings_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_department);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mApiService = ApiUtils.getAPIService();

        wingsSpinner = findViewById(R.id.spinnerForTypeOfWings);
        ivAddDept = findViewById(R.id.iv_sendAddDept);
        edit_enter = findViewById(R.id.edit_enter);
        add_image = findViewById(R.id.add_image);
        button_added = findViewById(R.id.button_added);
        ivBack = findViewById(R.id.iv_backBtn);
        iv_NO_Records = findViewById(R.id.iv_NO_Records);


        ivAddDept.setVisibility(View.GONE);
        ivAddDept.setOnClickListener(this);
        list = new ArrayList<>();
        checker = new ArrayList<>();
        checkedArrayList = new ArrayList<>();
        unCheckedArrayList = new ArrayList<>();
        maker = new ArrayList<>();
        arrayListWings = new ArrayList<>();


        button_added.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        recycler_view = findViewById(R.id.recycler_view);

        setSpinnerForRolesType();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(wingsSpinner);
            popupWindow.setHeight(300);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addValuesIntoList();
                InputMethodManager imm = (InputMethodManager) UpdateDepartmentActivity.this.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }

        });


    }

    private void addValuesIntoList() {

        String value = "";
        String editvalue = edit_enter.getText().toString();

        if (editvalue.equals("")) {
            Toast.makeText(UpdateDepartmentActivity.this, "Please enter the Value", Toast.LENGTH_SHORT).show();
        } else if (wingsSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(UpdateDepartmentActivity.this, "Please select wings", Toast.LENGTH_SHORT).show();
        } else {
            if (list.size() > 0) {
                boolean b = true;
                for (int i = 0; i < list.size(); i++) {
                    if ((list.get(i).getName()).contains(editvalue)) {
                        b = false;
                        Toast.makeText(UpdateDepartmentActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                    }
                }
                if (b) {
                    list.add(new Data(editvalue, true));

                }
            } else {
                list.add(new Data(editvalue, true));
            }

            recyclerAdapter = new UpdateRecyclerAdapter(this, list, Constant.DEPARTMENT_FRAG);
            recycler_view.setAdapter(recyclerAdapter);
            edit_enter.setText("");


        }
    }

    private void setSpinnerForRolesType() {

        arrayListWings.add(0, "Select Wings");
        mApiService.gettingWings(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                Log.d("jsonSliderKey", key.toString());

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("jsonSliderValue", jsonObjectValue.toString());

                                String wing_name = jsonObjectValue.getString("wing_name");
                                wings_status = jsonObjectValue.getBoolean("status");

                                if (wings_status) {
                                    arrayListWings.add(wing_name);
                                }

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(UpdateDepartmentActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, arrayListWings);
        wingsSpinner.setAdapter(customSpinnerAdapter);
        wingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();


                // if (position != 0) {
                wingsValue = parent.getSelectedItem().toString();
                Constant.wingName = wingsValue;
                try {
                    array.put(Constant.wingName);
                    jsonObject.put("wings", array);
                    Constant.DEPART_NAME = Constant.wingName;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                checkDepartmentValues(jsonObject);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void checkDepartmentValues(JSONObject jsonObject) {

        if (list.size() != 0) {
            list.clear();
            recyclerAdapter.notifyDataSetChanged();
        }

        mApiService.gettingDept(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        list.clear();
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");

                            JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.DEPART_NAME);

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("name");
                                boolean wings_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));

                                list.add(new Data(wing_name, wings_status));


                                if (list.size() > 0) {
                                    iv_NO_Records.setVisibility(View.GONE);
                                    recycler_view.setVisibility(View.VISIBLE);
                                    recyclerAdapter = new UpdateRecyclerAdapter(UpdateDepartmentActivity.this, list,
                                            Constant.DEPARTMENT_FRAG);
                                    recycler_view.setAdapter(recyclerAdapter);
                                } else {
                                    iv_NO_Records.setVisibility(View.GONE);
                                    recycler_view.setVisibility(View.VISIBLE);
                                }


                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("DEPT_FAIL", String.valueOf(response.code()));
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        list.clear();
                        iv_NO_Records.setVisibility(View.VISIBLE);
                        recycler_view.setVisibility(View.GONE);
                        // Toast.makeText(UpdateRolesActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_backBtn:
                Intent in = new Intent(UpdateDepartmentActivity.this, DashboardSettingActivity.class);
                startActivity(in);
                finish();
                break;


            case R.id.button_added:


                JSONArray array = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Data data = list.get(i);
                    if (data.isSelect()) {
                        Log.d("deptNameChecked", "" + data.getName());
                        array.put(data.getName());
                    }
                }

                JSONObject departments = new JSONObject();
                JSONObject obj = new JSONObject();
                for (int i = 0; i < list.size(); i++) {

                    boolean strListDataStatus = list.get(i).isSelect();
                    Log.i("strListDataStatus****", "" + strListDataStatus);

                    if (strListDataStatus) {
                        Log.i("strListDataStatus****1", "" + strListDataStatus);

                        JSONObject objDeptments = new JSONObject();

                        try {

                            objDeptments.put("name", list.get(i).getName());

                            objDeptments.put("status", String.valueOf(list.get(i).isSelect()));

                            departments.put(String.valueOf(i + 1), objDeptments);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {
                            obj.put("departments", departments);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Log.i("list***departments*", "" + departments + "***" + obj);

                    } else {
                        Log.i("strListDataStatus****1A", "" + strListDataStatus);

                    }


                }


                if (obj.toString().equals("{}")) {
                    Toast.makeText(UpdateDepartmentActivity.this, "Please add at least one Department", Toast.LENGTH_SHORT).show();

                } else {
                    callAddDepartmentAPI(obj);
                    utilities.displayProgressDialog(UpdateDepartmentActivity.this, "Processing ...", false);

                }

                break;
        }
    }

    private void callAddDepartmentAPI(JSONObject obj) {
        Log.d("askjdhb12", "" + obj);
        Log.d("askjdhb12", "" + Constant.SCHOOL_ID + "**" + Constant.EMPLOYEE_BY_ID + "**" + wingsValue);

        mApiService.addingDept(Constant.SCHOOL_ID, wingsValue.trim(), obj, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d("askjdhb1111", "" + response.raw());
                        Log.d("askjdhb", "" + response.body());


                        if (response.isSuccessful()) {

                            utilities.cancelProgressDialog();
                            Log.d("askjdhb", "" + response.body());

                            Toast.makeText(UpdateDepartmentActivity.this, "Department Added  Successfully. ", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                assert response.errorBody() != null;
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                Toast.makeText(UpdateDepartmentActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        utilities.cancelProgressDialog();
                    }
                });
    }


}
