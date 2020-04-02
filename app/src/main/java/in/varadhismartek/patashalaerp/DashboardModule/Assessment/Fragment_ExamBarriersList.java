package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ExamBarriersList extends Fragment implements View.OnClickListener {
    Spinner spExamType, spDivision, spClass;
    APIService apiService;
    private ArrayList<String> listExamType;
    private ArrayList<String> listDivision;
    private ArrayList<String> listClass;
    private String strExamType,strDiv,str_class,StrSubject = "", StrDuration = "", str_subject = "";
    long longMin, longmax;
    RecyclerView recyclerView;
    private ArrayList<AssesmentModel> assesmentModels;
    FloatingActionButton fab;
    AssessmentAdapter adapter;
    public Fragment_ExamBarriersList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exambarrier_list, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView(view);
        initListener();

        getExamType();


    return  view;
    }

    private void initListener() {
        fab.setOnClickListener(this);
    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        spExamType = view.findViewById(R.id.spExamType);
        spDivision = view.findViewById(R.id.spDivision);
        spClass = view.findViewById(R.id.spClass);
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);

        listExamType = new ArrayList<>();
        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        assesmentModels = new ArrayList<>();
    }

    private void getExamType() {
        listExamType.clear();
        listExamType.add("Exam Type");
        apiService.getExamList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("Exam_getlist *", "" + response.body());

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Sucess")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            Log.i("exam_name *", "" + jsonObject1);
                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                String exam_name = jsonObjectValue.getString("exam_type");
                                boolean exam_status = jsonObjectValue.getBoolean("status");
                                Log.i("exam_name *", "" + exam_name);

                                if (exam_status) {

                                    listExamType.add(exam_name);

                                }

                            }
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listExamType);
                            spExamType.setAdapter(customSpinnerAdapter);

                        }
                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        spExamType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strExamType = spExamType.getItemAtPosition(position).toString();
                if (strExamType.equalsIgnoreCase("Exam Type")){
                    spDivision.setSelection(0);
                    spClass.setSelection(0);
                }else {
                    spDivision.setSelection(0);
                    spClass.setSelection(0);
                }

                getDivisionApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDivisionApi() {
        listDivision.clear();
        listDivision.add("Division");
        apiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
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

                                    listDivision.add(division);
                                }


                            }
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDiv = parent.getSelectedItem().toString();
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                if (strDiv.equalsIgnoreCase("Division")){
                  //  spClass.setSelection(0);

                }
                else {

                    try {
                        array.put(strDiv);
                        jsonObject.put("divisions", array);
                        Constant.DIVISION_NAME = strDiv;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                getClassList(jsonObject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getClassList(JSONObject jsonObject) {
        listClass.clear();
        listClass.add("Class");
        apiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("CLASS_SECTIONDDD", "" + response.body());

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                              //  modelArrayList.clear();
                              //  customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();


                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    listClass.add(className);


                                }
                            }


                        }

                    } catch (JSONException je) {

                    }

                } else {

                    listClass.clear();
                    listClass.add("Class");

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


       CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spClass.setAdapter(customSpinnerAdapter);


        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class= spClass.getSelectedItem().toString();
                if (str_class.equalsIgnoreCase("Class")){

                }else {
                    getExamBarrierApi();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void getExamBarrierApi() {
        assesmentModels.clear();
        apiService.getExamBarriers(Constant.SCHOOL_ID, strExamType, strDiv, str_class).
                enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("Get_examBarrier**A", "" +response.code()+"**"+ response.body());
                        if (response.isSuccessful()) {
                            Log.i("Get_examBarrier**", "" + response.body());

                            try {

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");

                                if (status.equalsIgnoreCase("Sucess")) {

                                    if (object.getJSONObject("data").toString().equals("{}")) {
//                                        assesmentModels.clear();
                                       //  adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();


                                    } else {
                                        JSONObject jsonObject1 = object.getJSONObject("data");
                                        Iterator<String> keys = jsonObject1.keys();

                                        while (keys.hasNext()) {
                                            String key = keys.next();
                                            JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                            Log.d("jsonSliderValue", jsonObjectValue.toString());

                                            boolean gradeStatus = jsonObjectValue.getBoolean("status");

                                            if (gradeStatus) {

                                                StrSubject = key;
                                                StrDuration = jsonObjectValue.getLong("exam_duration")+"";
                                                longMin = jsonObjectValue.getLong("min_marks");
                                                longmax = jsonObjectValue.getLong("max_marks");

                                                assesmentModels.add(new AssesmentModel(longMin, longmax, strExamType,
                                                        strDiv, str_class, StrSubject, StrDuration));

                                            }

                                        }

                                    }
                                    setRecyclerView();
                                }
                            } catch (JSONException je) {

                            }
                        }
                        else {
                            try {
                                assert response.errorBody()!=null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }

    private void setRecyclerView() {
         adapter = new AssessmentAdapter(getActivity(), assesmentModels, Constant.RV_EXAMS_BARRIER_ROW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.fab:
        ExamActivity examActivity = (ExamActivity)getActivity();
        examActivity.loadFragment(Constant.ADD_EXAM_BARRIER,null);
        break;
}
    }
}
