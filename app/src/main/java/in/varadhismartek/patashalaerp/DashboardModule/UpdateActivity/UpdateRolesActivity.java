package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.HashMap;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
import in.varadhismartek.patashalaerp.DashboardModule.BarrierSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateRecyclerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRolesActivity extends AppCompatActivity implements View.OnClickListener {
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
    Spinner spWings, spDept;
    APIService mApiService;
    ImageView ivSendRoles, backbutton,iv_NORecords;

    private ArrayList<AddWingsModel> addWingsModelArrayList;


    private ProgressDialog progressDialog = null;
    ArrayList<String> arrayListWings;
    ArrayList<String> arrayListDept;
    private String wingsValue;
    private String deptValue;
    JSONArray wingsArray;
    JSONObject wingsJsonObject = new JSONObject();
    Utilities utilities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_barriers);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        list = new ArrayList<>();

        mApiService = ApiUtils.getAPIService();
        ivSendRoles = findViewById(R.id.iv_sendAddRoles);
        backbutton = findViewById(R.id.iv_backBtn);
        iv_NORecords = findViewById(R.id.iv_NORecords);
        ivSendRoles.setVisibility(View.GONE);
        ivSendRoles.setOnClickListener(this);
        backbutton.setOnClickListener(this);
        checker = new ArrayList<>();
        checkedArrayList = new ArrayList<>();
        unCheckedArrayList = new ArrayList<>();
        maker = new ArrayList<>();

        edit_enter = findViewById(R.id.edit_enter);
        add_image = findViewById(R.id.add_image);
        button_added = findViewById(R.id.button_added);
        recycler_view = findViewById(R.id.recycler_view);


        recyclerAdapter = new UpdateRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(UpdateRolesActivity.this, 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);


        spWings = findViewById(R.id.wings);
        spDept = findViewById(R.id.dept);
        addWingsModelArrayList = new ArrayList<>();

        arrayListWings = new ArrayList<>();
        fetchWingsAndDepartment();


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spWings);
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spDept);
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        //This is for Add the new Selectable values

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = "";
                String editvalue = edit_enter.getText().toString();

                if (spWings.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateRolesActivity.this, "Please select wings", Toast.LENGTH_SHORT).show();
                } else if (spDept.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateRolesActivity.this, "Please select dept", Toast.LENGTH_SHORT).show();

                } else if (editvalue.equals("")) {
                    Toast.makeText(UpdateRolesActivity.this, "Please enter the Value", Toast.LENGTH_SHORT).show();
                } else {


                    if (list.size() > 0) {
                        boolean b = true;
                        for (int i = 0; i < list.size(); i++) {
                            if ((list.get(i).getName()).contains(editvalue)) {
                                b = false;
                                Toast.makeText(UpdateRolesActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {
                            list.add(new Data(editvalue, true));

                        }
                    }
                    else {
                        list.add(new Data(editvalue, true));
                    }

                    if (list.size()>0){
                        recycler_view.setVisibility(View.VISIBLE);
                        iv_NORecords.setVisibility(View.GONE);
                        recyclerAdapter = new UpdateRecyclerAdapter(UpdateRolesActivity.this, list,
                                Constant.BARRIERS_FRAG);
                        recycler_view.setAdapter(recyclerAdapter);

                    }else {
                        recycler_view.setVisibility(View.GONE);
                        iv_NORecords.setVisibility(View.VISIBLE);

                    }


                    edit_enter.setText("");

                    InputMethodManager imm = (InputMethodManager) UpdateRolesActivity.this.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }

            }

        });

        //This is for collect the Selectable values
        button_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spWings.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateRolesActivity.this, "Please select wings", Toast.LENGTH_SHORT).show();
                } else if (spDept.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateRolesActivity.this, "Please select dept", Toast.LENGTH_SHORT).show();

                } else {
                    //Log.d("WingsDept", wingsValue+deptValue);


                    JSONObject roles = new JSONObject();
                    JSONObject obj = new JSONObject();

                    Log.i("strListDataStatus****11", "" + list.size());
                    for (int i = 0; i < list.size(); i++) {

                        boolean strListDataStatus = list.get(i).isSelect();
                        Log.i("strListDataStatus****", "" + strListDataStatus);

                        if (strListDataStatus) {
                            Log.i("strListDataStatus****1", "" + strListDataStatus);


                            JSONObject rolesObj = new JSONObject();
                            try {
                                rolesObj.put("name", list.get(i).getName());
                                rolesObj.put("status", String.valueOf(list.get(i).isSelect()));
                                roles.put(String.valueOf(i + 1), rolesObj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                obj.put("roles", roles);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Log.i("list***roles*", "" + roles + "***" + obj);

                        } else {
                            Log.i("strListDataStatus****1A", "" + strListDataStatus);

                        }
                    }

                    if (obj.toString().equals("{}")) {
                        Toast.makeText(UpdateRolesActivity.this, "Please add at least one Roles", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.i("ROLES_Json****", "" + obj);
                        addRolesMethod(wingsValue, deptValue, obj);

                        utilities.displayProgressDialog(UpdateRolesActivity.this, "Processing ...", false);

                    }


                }


            }
        });
    }





    private void addRolesMethod(String wingsValue, String deptValue, JSONObject obj) {
        mApiService.addWingRoles(Constant.SCHOOL_ID, wingsValue, deptValue, obj, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {
                            Log.i("ROLES_Res****", "" + response.body());
                            utilities.cancelProgressDialog();
                            Toast.makeText(UpdateRolesActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                assert response.errorBody() != null;
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                Toast.makeText(UpdateRolesActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        utilities.cancelProgressDialog();
                        Log.i("ERROR_ROLES",""+t.toString());

                    }
                });

    }

    private void fetchWingsAndDepartment() {
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

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("wing_name");
                                boolean wings_status = jsonObjectValue.getBoolean("status");
                                if (wings_status){
                                    arrayListWings.add(wing_name);
                                }


                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, arrayListWings);
        spWings.setAdapter(customSpinnerAdapter);
        spWings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wingsArray = new JSONArray();
                list.clear();
                recyclerAdapter.notifyDataSetChanged();

                //  if (position != 0) {
                wingsValue = parent.getSelectedItem().toString();
                Constant.wingName = wingsValue;
                try {
                    wingsArray.put(Constant.wingName);
                    wingsJsonObject.put("wings", wingsArray);
                    Constant.DEPART_NAME = Constant.wingName;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getDeptBasedOnWings(wingsJsonObject);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getDeptBasedOnWings(final JSONObject wingsJsonObject) {
        arrayListDept = new ArrayList<>();
        arrayListDept.clear();
        arrayListDept.add(0, "Select Department");
        mApiService.gettingDept(Constant.SCHOOL_ID, wingsJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {

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
                                if (wings_status){
                                    arrayListDept.add(wing_name);
                                }


                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(UpdateRolesActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Role ERROR", "" + t.toString());

            }
        });

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(UpdateRolesActivity.this , arrayListDept);
        spDept.setAdapter(customSpinnerAdapter);
        spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONArray deptArray = new JSONArray();
                JSONObject deptJsonObject = new JSONObject();

                if (position != 0) {

                    deptValue = parent.getSelectedItem().toString();
                    Constant.deptName = deptValue;

                    try {
                        Constant.ROLES_NAME = deptValue;
                        deptArray.put(Constant.deptName);
                        deptJsonObject.put("departments", deptArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Constant.ROLES_NAME = "roles";
                        deptArray.put("All");
                        deptJsonObject.put("departments", deptArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                getRolesBasedOnDepartment(wingsJsonObject, deptJsonObject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getRolesBasedOnDepartment(JSONObject wingsJsonObject, JSONObject deptJsonObject) {
        if (list.size() != 0) {
            list.clear();

            recyclerAdapter.notifyDataSetChanged();
        }


        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d("ADMIN_API_json1", ""+response.body()+response.code());

                        list.clear();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));


                                String status = (String) json1.get("status");

                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonObject1 = json1.getJSONObject("data");
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.ROLES_NAME);


                                    Iterator<String> keys = jsonObject2.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                        String rolesName = jsonObjectValue.getString("role");
                                        boolean roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));

                                        if (roles_status){
                                            list.add(new Data(rolesName, roles_status));
                                        }
                                        if (list.size()>0){
                                            recycler_view.setVisibility(View.VISIBLE);
                                            iv_NORecords.setVisibility(View.GONE);
                                            recyclerAdapter = new UpdateRecyclerAdapter(UpdateRolesActivity.this, list,
                                                    Constant.BARRIERS_FRAG);
                                            recycler_view.setAdapter(recyclerAdapter);

                                        }else {
                                            recycler_view.setVisibility(View.GONE);
                                            iv_NORecords.setVisibility(View.VISIBLE);

                                        }

                  /*              recyclerAdapter = new UpdateRecyclerAdapter(UpdateRolesActivity.this, list,
                                        Constant.BARRIERS_FRAG);
                                recycler_view.setAdapter(recyclerAdapter);*/
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                assert response.errorBody() != null;
                                utilities.cancelProgressDialog();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                list.clear();
                                // Toast.makeText(UpdateRolesActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("ERROr",""+t.toString());

                    }
                });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_sendAddRoles:
                if (list.size() > 0) {
                    Intent in = new Intent(UpdateRolesActivity.this, DashboardActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    Toast.makeText(UpdateRolesActivity.this , "Please Add Roles", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_backBtn:
                Intent in = new Intent(UpdateRolesActivity.this, DashboardSettingActivity.class);
                startActivity(in);
                finish();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateRolesActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}