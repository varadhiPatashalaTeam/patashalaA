package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class Fragment_FeeBarriers extends Fragment implements View.OnClickListener {
    Spinner spfeeType, spClass;
    APIService apiService, mApiService;
    ArrayList<String> classList;
    ArrayList<String> divisionList = new ArrayList<>();
    ArrayList<String> feeTypeList = new ArrayList<>();
    String strFType, strSpClass;
    TextView tvAdd;
    String str_toDate = "", startYear = "", endYear = "", sDate = "", eDate = "", strSelectSession = "", SubfromDate = "";
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayList<String> spinnerDateList = new ArrayList<>();
    Spinner spn_academicyear;
    private int year, month, date;
    EditText edAmount;
    RecyclerView recycler_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fees_barreries, container, false);
        initView(view);
        getAllClasses();
        getFeeType();
        getAcadmicAPI();

        getfeeInClass();
        return view;
    }

    private void getfeeInClass() {
        apiService.getFeeInClass(Constant.SCHOOL_ID,"2019-12-22").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Log.d("jsonObject1", jsonObject1.toString());
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                Log.i("GET_Fees**key",""+key);
                                JSONObject jsonClassKey = jsonObject1.getJSONObject(key);
                                Log.d("jsonClassKey", jsonClassKey.toString());
                                if (jsonClassKey.equals("[]")){
                                    Log.i("GET_Fees**77","777");

                                }else{
                                    Log.i("GET_Fees**88","778");
                                }

                            }

                        }
                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getFeeType() {
        apiService.getFeeStructure(Constant.SCHOOL_ID, "2019-12-22")
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {
                            feeTypeList.clear();
                            feeTypeList.add("-Fee Type-");
                            Log.i("GET_Fees**", "" + response.body() + "r**" + response.code());
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject1 = json1.getJSONObject("data");
                                    Iterator<String> keys = jsonObject1.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                        Log.d("jsonSliderValue", jsonObjectValue.toString());


                                        String strFeeType = jsonObjectValue.getString("fee_type");
                                        feeTypeList.add(strFeeType);

                                    }
                                }
                                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), feeTypeList);
                                spfeeType.setAdapter(customSpinnerAdapter);
                            } catch (JSONException je) {

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

        spfeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strFType = spfeeType.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getAllClasses() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Class_api_0", String.valueOf(divisionList.size()));

                progressDialog.dismiss();

                // if (divisionList.size() > 0) {

                Log.d("Class_api_1", String.valueOf(divisionList.size()));

                JSONObject object = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                jsonArray.put("All");


                try {
                    object.put("divisions", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Class_api_2", object.toString());

                if (classList.size() > 0)
                    classList.clear();
                classList.add("-Class-");
                apiService.getClassSections(Constant.SCHOOL_ID, object).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        if (response.isSuccessful()) {
                            try {

                                JSONObject obj = null;

                                obj = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("myDivisionKey_data2", String.valueOf(response.code()));

                                //JSONObject obj = new JSONObject(new Gson().toJson(response.body()));

                                String status = obj.getString("status");
                                Log.d("myDivisionKey_data2", obj.toString());


                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonData = obj.getJSONObject("data");
                                    Log.d("myDivisionKey_data1", jsonData.toString());


                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()) {

                                        String myDivisionKey = key.next();
                                        Log.d("myDivisionKey", myDivisionKey);

                                        JSONObject keyData = jsonData.getJSONObject(myDivisionKey);
                                        Log.d("myDivisionKey_data", keyData.toString());

                                        Iterator<String> classKey = keyData.keys();

                                        while (classKey.hasNext()) {

                                            JSONObject classData = keyData.getJSONObject(classKey.next());
                                            Log.d("classData", classData.toString());

                                            String class_name = classData.getString("class_name");
                                            Log.d("className", class_name);
                                            classList.add(class_name);


                                            JSONArray jsonArray1 = classData.getJSONArray("sections");
                                            Log.d("classData_array", jsonArray1.toString());

                                            ArrayList<String> sections = new ArrayList<>();

                                            for (int i = 0; i < jsonArray1.length(); i++) {

                                                sections.add(jsonArray1.getString(i));
                                                Log.d("classData_array " + i, jsonArray1.getString(i));

                                            }

                                            //   classSectionList.add(new NotificationModel(class_name, sections));
                                        }
                                    }

                                }

                                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), classList);
                                spClass.setAdapter(customSpinnerAdapter);

                                spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        strSpClass = parent.getItemAtPosition(position).toString();


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("create_API", message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });

            }

            // }
        }, 2000);


    }


    private void getAcadmicAPI() {

        mApiService.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.d("SESSION**AYEAR", "onResponse: " + response.body());
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("Academic_years");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String resStartDate = jsonObjectValue.getString("start_date");
                                String resEndDate = jsonObjectValue.getString("end_date");


                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");

                                try {
                                    Date fromYear = formater.parse(resStartDate);
                                    Date toYear = formater.parse(resEndDate);

                                    sDate = formatterDate.format(fromYear);
                                    eDate = formatterDate.format(toYear);

                                    startYear = formatterYear.format(fromYear);
                                    endYear = formatterYear.format(toYear);

                                    String selectedDate = sDate + " - " + eDate;
                                    String selectedYear = startYear + " - " + endYear;

                                    spinnerList.add(selectedYear);
                                    spinnerDateList.add(selectedDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                            setSpinner();


                        }
                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setSpinner() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), spinnerList);
        spn_academicyear.setAdapter(adapter);
        spn_academicyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strAcadmicYear = spn_academicyear.getItemAtPosition(position).toString();
                if (!strAcadmicYear.equalsIgnoreCase("Select Academic year")) {
                    int pos = parent.getSelectedItemPosition();


                    strSelectSession = spinnerDateList.get(pos);
                    String str_sessionName = spn_academicyear.getSelectedItem().toString();
                    System.out.println("str_sessionName**1**" + spinnerDateList.get(pos) + "****" + str_sessionName);
                    //SubtoDate = strSelectSession.substring(13);

                    SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));
                    Constant.ACADEMIC_YEAR = SubfromDate;
                    Log.d(TAG, "onResponse:getsession " + SubfromDate + "***");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        mApiService = ApiUtilsPatashala.getService();
        spClass = view.findViewById(R.id.sp_class);
        spfeeType = view.findViewById(R.id.sp_feeType);
        tvAdd = view.findViewById(R.id.add);
        spn_academicyear = view.findViewById(R.id.sp_acdaemicyear);
        edAmount = view.findViewById(R.id.fee_amount);
        recycler_view = view.findViewById(R.id.recycler_view);
        tvAdd.setOnClickListener(this);

        classList = new ArrayList<>();


    }

    @Override
    public void onClick(View v) {

        if (strFType.equalsIgnoreCase("-Fee Type") || (strSpClass.equalsIgnoreCase("Class"))) {
            Toast.makeText(getActivity(), "Select all fields", Toast.LENGTH_SHORT).show();

        } else {

            apiService.addfeeByClass(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID, SubfromDate, "2", strFType, strSpClass,
                    edAmount.getText().toString()).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()) {
                        Log.i("respone_fee", "" + response.body());
                        Toast.makeText(getActivity(), "Fee Barrier added", Toast.LENGTH_SHORT).show();
                    } else {
                        assert response.errorBody() != null;
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Log.d("create_API", message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }
    }
}
