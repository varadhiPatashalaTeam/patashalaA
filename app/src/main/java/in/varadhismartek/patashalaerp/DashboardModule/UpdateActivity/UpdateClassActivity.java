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

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionAdapter;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.ClassAndSection.DivisionClassModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter.UpdateDivisionAdapter;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateClassActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_backBtn, iv_sendAddClasses,iv_NORecords;
    EditText edit_enter;
    ImageButton add_image;
    RecyclerView recycler_view;
    Spinner spinnerForDivision;
    ArrayList<DivisionClassModel> divisionClassModels;
    ArrayList<ClassSectionModel> list;
    ArrayList<ClassSectionAndDivisionModel> classList, newClassList;

    ArrayList<String> listDivision;
    ProgressDialog progressDialog;
    APIService mApiService;
    Button button_added;
    String divisionValue = "";
    CustomSpinnerAdapter customSpinnerAdapter;
    ClassSectionAdapter classSectionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_classes);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        progressDialog = new ProgressDialog(UpdateClassActivity.this);
        mApiService = ApiUtils.getAPIService();

        iv_backBtn = findViewById(R.id.iv_backBtn);
        spinnerForDivision = findViewById(R.id.spinnerForDivision);
        edit_enter = findViewById(R.id.edit_enter);
        add_image = findViewById(R.id.add_image);
        button_added = findViewById(R.id.button_added);
        recycler_view = findViewById(R.id.recycler_view);
        iv_sendAddClasses = findViewById(R.id.iv_sendAddClasses);
        iv_NORecords = findViewById(R.id.iv_NORecords);

        iv_sendAddClasses.setVisibility(View.GONE);
        button_added.setVisibility(View.GONE);

        list = new ArrayList<>();
        listDivision = new ArrayList<>();
        classList = new ArrayList<>();

        newClassList = new ArrayList<>();
        divisionClassModels = new ArrayList<>();
        listDivision.clear();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(UpdateClassActivity.this, 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

        iv_backBtn.setOnClickListener(this);
        add_image.setOnClickListener(this);
        iv_sendAddClasses.setOnClickListener(this);
        button_added.setOnClickListener(this);

        getDivisionApi();

    }

    private void getDivisionApi() {
        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "Select Division");
                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");
                                    String school_id = object1.getString("school_id");
                                    String added_by_id = object1.getString("added_by_id");

                                    listDivision.add(division);
                                }

                                setSpinnerForDivision();
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(UpdateClassActivity.this, "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

    }

    private void setSpinnerForDivision() {

        customSpinnerAdapter = new CustomSpinnerAdapter(UpdateClassActivity.this, listDivision);
        spinnerForDivision.setAdapter(customSpinnerAdapter);

        spinnerForDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classList.clear();
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                divisionValue = parent.getSelectedItem().toString();
                System.out.println("divisionValue****" + divisionValue);

                if (!divisionValue.equalsIgnoreCase("Select Division")) {
                    Constant.Division = divisionValue;
                    try {
                        array.put(Constant.Division);
                        jsonObject.put("divisions", array);
                        Constant.DIVISION_NAME = Constant.Division;
                        // getClassToDataBase();
                        getClassSectionList(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                System.out.println("jsonObject****" + jsonObject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getClassSectionList(JSONObject jsonObject) {
        Log.i("DATA****", "" + Constant.SCHOOL_ID + "**" + jsonObject);

        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTION", "" + response.raw() + response.code() + response.body());

                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                classList.clear();
                                //Toast.makeText(UpdateClassActivity.this, "No Data", Toast.LENGTH_SHORT).show();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(Constant.Division);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");


                                    classList.add(new ClassSectionAndDivisionModel(className, true));

                                }

                            }
                            if (classList.size()>0){
                                iv_NORecords.setVisibility(View.GONE);
                                recycler_view.setVisibility(View.VISIBLE);

                                UpdateDivisionAdapter recyclerAdapter = new UpdateDivisionAdapter(UpdateClassActivity.this, classList,
                                        Constant.CLASS_ROW);
                                recycler_view.setAdapter(recyclerAdapter);

                            }else {
                                iv_NORecords.setVisibility(View.VISIBLE);
                                recycler_view.setVisibility(View.GONE);
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(UpdateClassActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(UpdateClassActivity.this, DashboardSettingActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.add_image:
                String editvalue = edit_enter.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(UpdateClassActivity.this, "Please enter the Value", Toast.LENGTH_SHORT).show();
                } else if (spinnerForDivision.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateClassActivity.this, "Please Select Division", Toast.LENGTH_SHORT).show();
                } else {
                    if (classList.size() > 0) {

                        boolean b = true;
                        for (int i = 0; i < classList.size(); i++) {
                            if ((classList.get(i).getName()).equalsIgnoreCase(editvalue)) {
                                b = false;
                                Toast.makeText(UpdateClassActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {

                            classList.add(new ClassSectionAndDivisionModel(editvalue, true));
                            Log.i("CLASS**1", "" + editvalue + classList.size());
                            addClassAPI(editvalue);
                        }

                    } else {
                        classList.add(new ClassSectionAndDivisionModel(editvalue, true));
                        Log.i("CLASS**2", "" + editvalue + classList.size());

                        addClassAPI(editvalue);
                    }


                    edit_enter.setText("");

                    InputMethodManager imm = (InputMethodManager) UpdateClassActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }

                break;
        }

    }

    private void addClassAPI(String editvalue) {

        int findIndex = (classList.size() - 1);
        if (findIndex == 0) {
            findIndex = 1;
        } else {
            findIndex = findIndex;
        }
        Log.i("CLASS**3", "" + editvalue + classList.size() + "**" + findIndex);

        String strSec = "A";
        JSONObject jsonClass = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            JSONObject jsonSection = new JSONObject();

            array.put(strSec);
            jsonSection.put("sections", array);
            jsonSection.put("order", findIndex);
            jsonClass.put(editvalue.trim(), jsonSection);

            mApiService.addClassSections(Constant.SCHOOL_ID, Constant.Division,
                    jsonClass, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        try {

                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            String status = (String) object.get("status");

                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(UpdateClassActivity.this,"Class Added successfully",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure (Call < Object > call, Throwable t){

                }
            });

        } catch(JSONException e){
            e.printStackTrace();
        }

        Log.i("CLASS**3", "" + editvalue + classList.size() + "**" + findIndex + "***" + jsonClass);

    }
}
