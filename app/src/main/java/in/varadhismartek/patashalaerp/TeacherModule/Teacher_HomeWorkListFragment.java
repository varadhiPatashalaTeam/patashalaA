package in.varadhismartek.patashalaerp.TeacherModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Teacher_HomeWorkListFragment extends Fragment {
    APIService mApiService;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSectionFilter;

    ArrayList<ClassSectionModel> modelArrayList;
    CustomSpinnerAdapter customSpinnerAdapter;
    Spinner spDivision, spnClass, spSection;
    String strDiv = "", str_class = "ukg", str_toDate = "", endYear = "", currentDate = "";
    ArrayList<TeacherHomeworkModle> teacherHomeworkModles;
    RecyclerView recycler_view;

    String completePercentage = "", note = "", reportDate = "", assignerID = "", assigerFName = "", assignDate = "",
            assignerLName = "", startDate = "", endDate = "",
            studentFName = "", studentLName = "", StudentId = "", homeTitle = "",
            homeDescription = "", homeClass = "", homeSubject = "", homeSection = "";
    TeacherHomeworkAdapter teacherHomeworkAdapter;

    public Teacher_HomeWorkListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework_list, container, false);
        initViews(view);
        initListeners();
        getDivisionApi();
        getHomeworkList();
        return view;
    }

    private void getHomeworkList() {
        mApiService.getHomeworkListTeacher(Constant.SCHOOL_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("HOME_TEACHER*S*", "" + response.body());
                        Log.i("HOME_TEACHER**", "" + response.code());

                        if (response.isSuccessful()) {
                            try {
                                teacherHomeworkModles.clear();
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                System.out.println("MEssage**S*" + object.getString("message"));
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");

                                    Iterator<String> keys = jsonObject.keys();


                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject objectData = jsonObject.getJSONObject(key);

                                        System.out.println("completePercentage***1 " + objectData);
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");
                                        if (!objectData.has("assigner_id")) {
                                            assignerID = "no assigner";
                                        }
                                        //assignerID =objectData.getString("assigner_id");
                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                        // assignDate =objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                completePercentage, note, reportDate, assignerID
                                                , assigerFName,
                                                assignerLName, startDate, endDate,
                                                studentFName, studentLName, StudentId, homeTitle,
                                                homeDescription, homeClass, homeSubject, homeSection
                                        ));
                                    }

                                    setRecyclerView();
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


    private void initListeners() {
       /* fab.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        linearLayoutDate.setOnClickListener(this);*/
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        //  iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recycler_view = view.findViewById(R.id.recycler_view);
        spDivision = view.findViewById(R.id.sp_division);
        spnClass = view.findViewById(R.id.sp_class);
        spSection = view.findViewById(R.id.sp_section);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSectionFilter = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        teacherHomeworkModles = new ArrayList<>();

    }


    private void setRecyclerView() {

        teacherHomeworkAdapter = new TeacherHomeworkAdapter(teacherHomeworkModles, getActivity(),
                Constant.RV_TEACHER_HOMEWORK_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(teacherHomeworkAdapter);
        teacherHomeworkAdapter.notifyDataSetChanged();

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


                            }
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
                            spDivision.setAdapter(customSpinnerAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {

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

                try {
                    array.put(strDiv);
                    jsonObject.put("divisions", array);
                    Constant.DIVISION_NAME = strDiv;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getClassSectionList(jsonObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getClassSectionList(JSONObject jsonObject) {

        listClass.clear();
        listClass.add(0, "Select Class");
        customSpinnerAdapter.notifyDataSetChanged();
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("CLASS_SECTIONDDD", "" + response.body());
                Log.i("CLASS_SECTIONDDD", "" + response.code());

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                // listClass.clear();

                                //modelArrayList.clear();
                                // customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();

                            } else {

                                JSONObject jsonObject2 = jsonObject1.getJSONObject(strDiv);
                                Iterator<String> keys = jsonObject2.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                    String className = jsonObjectValue.getString("class_name");
                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("sections");

                                    listSection = new ArrayList<>();
                                    String Section = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Section = jsonArray.getString(i);
                                        listSection.add(Section);
                                    }

                                    listClass.add(className);
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                    spnClass.setAdapter(customSpinnerAdapter);

                                    modelArrayList.add(new ClassSectionModel(className, listSection));
                                }
                            }


                        }


                    } catch (JSONException je) {

                    }

                } else {
                    if (String.valueOf(response.code()).equals("400")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "No Class List", Toast.LENGTH_SHORT).show();
                        listClass.clear();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_class = parent.getItemAtPosition(position).toString();
                listSectionFilter.clear();
                listSectionFilter.add("Select Section");

                if (str_class.equalsIgnoreCase("Select Class")) {

                } else {

                    for (int j = 0; j < modelArrayList.size(); j++) {
                        if (modelArrayList.get(j).getClassName().contains(str_class)) {
                            for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {
                                listSectionFilter.add(modelArrayList.get(j).getListSection().get(k));

                            }
                            System.out.println("listSectionFilter***" + listSectionFilter);
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionFilter);
                            spSection.setAdapter(customSpinnerAdapter);

                        }
                    }
                    getHomeworkByClass(str_class);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strSection = spSection.getItemAtPosition(position).toString();
                if (strSection.equalsIgnoreCase("Select Section")) {
                    Toast.makeText(getActivity(), "Select Valid Section", Toast.LENGTH_SHORT).show();
                    getHomeworkByClass(str_class);
                } else {
                    getHomeworkByClassSection(str_class, strSection);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getHomeworkByClass(String str_class) {
        mApiService.getHomeworkListTeacherClass(Constant.SCHOOL_ID, str_class)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        teacherHomeworkModles.clear();
                        teacherHomeworkAdapter.notifyDataSetChanged();

                        Log.i("HOME_TEACHER**C**", "" + response.body());

                        if (response.isSuccessful()) {
                            try {
                                teacherHomeworkModles.clear();
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                System.out.println("MEssage**C*" + object.getString("message"));
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");

                                    Iterator<String> keys = jsonObject.keys();


                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject objectData = jsonObject.getJSONObject(key);

                                        System.out.println("completePercentage***1 " + objectData);
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");
                                        if (!objectData.has("assigner_id")) {
                                            assignerID = "no assigner";
                                        } else {
                                            assignerID = objectData.getString("assigner_id");
                                        }

                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                        // assignDate =objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                completePercentage, note, reportDate, assignerID
                                                , assigerFName,
                                                assignerLName, startDate, endDate,
                                                studentFName, studentLName, StudentId, homeTitle,
                                                homeDescription, homeClass, homeSubject, homeSection
                                        ));
                                    }

                                    setRecyclerView();
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


    private void getHomeworkByClassSection(String str_class, String strSection) {

        mApiService.HomeworkListByClassSection(Constant.SCHOOL_ID, str_class, strSection)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        teacherHomeworkModles.clear();
                        teacherHomeworkAdapter.notifyDataSetChanged();

                        Log.i("HOME_TEACHER**Se", "" + response.body());
                        Log.i("HOME_TEACHER**", "" + response.code());
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");

                                if (status.equalsIgnoreCase("success")) {
                                    JSONArray jsonArray = object.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++) {


                                        JSONObject objectData = jsonArray.getJSONObject(i);

                                        System.out.println("completePercentage***1 " + objectData);
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");
                                        if (!objectData.has("assigner_id")) {
                                            assignerID = "no assigner";
                                        }else {
                                            assignerID =objectData.getString("assigner_id");
                                        }

                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                        // assignDate =objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                completePercentage, note, reportDate, assignerID
                                                , assigerFName,
                                                assignerLName, startDate, endDate,
                                                studentFName, studentLName, StudentId, homeTitle,
                                                homeDescription, homeClass, homeSubject, homeSection
                                        ));


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
}
