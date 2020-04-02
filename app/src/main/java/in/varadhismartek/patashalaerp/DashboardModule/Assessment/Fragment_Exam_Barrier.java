package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
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

public class Fragment_Exam_Barrier extends Fragment implements View.OnClickListener {
    Spinner spExamType, spDivision, spClass, spSubject, spn_section;
    EditText edMinMarks, edMaxMark, edExamDuration;
    TextView tvAdd;
    APIService apiService;
    private ArrayList<String> listExamType;
    private ArrayList<AssesmentModel> assesmentModels;
    private ArrayList<String> listDivision;
    private ArrayList<String> listClass;
    ArrayList<String> listSectionNew;
    ArrayList<String> listSection;
    ArrayList<ClassSectionModel> modelArrayList;
    private ArrayList<String> listSubject;
    String strExamType = "", strDiv = "", str_class = "", strSubject = "", strSection = "", str_subject = "";
    private ArrayList<String> subjectList = new ArrayList<>();

    // RecyclerView recyclerView;
    String StrExamType, StrDiv, StrClass, StrSubject, StrDuration;
    long longMin, longmax;
    CustomSpinnerAdapter customSpinnerAdapter;
    AssessmentAdapter adapter;
    private ImageView iv_backBtn;
    String IntMaxValue = "0", IntMinValue = "0";

    public Fragment_Exam_Barrier() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_barrier, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView(view);
        initListener();
        getExamType();


        return view;
    }


    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        spExamType = view.findViewById(R.id.spExamType);
        spDivision = view.findViewById(R.id.spDivision);
        spClass = view.findViewById(R.id.spClass);
        spn_section = view.findViewById(R.id.spn_section);
        spSubject = view.findViewById(R.id.spSubject);
        tvAdd = view.findViewById(R.id.tvAdd);

        edMinMarks = view.findViewById(R.id.ed_minmarks);
        edMaxMark = view.findViewById(R.id.ed_maxmarks);
        edExamDuration = view.findViewById(R.id.ed_examDuration);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);


        listExamType = new ArrayList<>();
        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSubject = new ArrayList<>();
        assesmentModels = new ArrayList<>();
        listSectionNew = new ArrayList<>();
        modelArrayList = new ArrayList<>();


    }

    private void initListener() {
        tvAdd.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
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
                if (strExamType.equalsIgnoreCase("Exam Type")) {
                    spDivision.setSelection(0);
                    spClass.setSelection(0);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);

                } else {
                    spDivision.setSelection(0);
                    spClass.setSelection(0);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);

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

                if (strDiv.equalsIgnoreCase("Division")) {
                    spClass.setSelection(0);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);

                } else {
                    spClass.setSelection(0);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);
                    try {
                        array.put(strDiv);
                        jsonObject.put("divisions", array);
                        Constant.DIVISION_NAME = strDiv;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                                modelArrayList.clear();
                                customSpinnerAdapter.notifyDataSetChanged();
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

                                    modelArrayList.add(new ClassSectionModel(className, listSection));

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


        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
        spClass.setAdapter(customSpinnerAdapter);


        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                str_class = spClass.getSelectedItem().toString();

                listSectionNew.clear();
                listSubject.clear();
                listSectionNew.add("Section");

                if (str_class.equalsIgnoreCase("Class")) {
                    Log.i("ClassName***", "" + str_class);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);
                } else {
                    Log.i("ClassName***1", "" + str_class + "***" + modelArrayList);
                    spn_section.setSelection(0);
                    spSubject.setSelection(0);
                    boolean b = true;

                    for (int j = 0; j < modelArrayList.size(); j++) {
                        if (modelArrayList.get(j).getClassName().contains(str_class)) {

                            for (int k = 0; k < modelArrayList.get(j).getListSection().size(); k++) {

                                listSectionNew.add(modelArrayList.get(j).getListSection().get(k));

                            }
                        }
                    }

                }

                getSubject(strDiv, str_class);

                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSectionNew);
                spn_section.setAdapter(customSpinnerAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strSection = spn_section.getSelectedItem().toString();
                //getSubject(strDiv, str_class);
                if (strSection.equalsIgnoreCase("Section")) {
                    spSubject.setSelection(0);
                } else {
                    spSubject.setSelection(0);

                }
                // getSubject(strDiv, str_class);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSubject(final String strDiv, final String str_class) {
        apiService.getSubjectByClass(Constant.SCHOOL_ID, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("SubjectList**1", "" + response.code() + "**" + response.body());
                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            if (object.getJSONObject("Section").toString().equals("{}")) {

                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("Section");
                                Iterator<String> keys = jsonObject1.keys();
                                HashSet<String> hashSet = new HashSet<String>();
                                while (keys.hasNext()) {
                                    String sectionKey = keys.next();
                                    JSONObject jsonSection = jsonObject1.getJSONObject(sectionKey);

                                    Iterator<String> keys2 = jsonSection.keys();

                                    Log.i("SubjectList**key2", "" + sectionKey);
                                    while (keys2.hasNext()) {
                                        String subjectKey = keys2.next().trim();
                                        Log.i("SubjectList**key2", "subjectKey" + sectionKey);
                                        if (subjectKey.equals("{}")) {

                                        } else {
                                            JSONObject jsonSubject = jsonSection.getJSONObject(subjectKey);

                                            Log.i("SubjectList**3", "jsonSubject" + jsonSubject.toString());
                                            String strType = jsonSubject.getString("subject_type");
                                            String strCode = jsonSubject.getString("subject_code");
                                            boolean strStatus = jsonSubject.getBoolean("status");

                                            listSubject.add(subjectKey);
                                            hashSet.addAll(listSubject);
                                            listSubject.clear();
                                            listSubject.addAll(hashSet);

                                        }

                                    }
                                    Log.i("SubjectList**4", "" + listSubject + "**" + listSubject.size());
                                    if (listSubject.size() > 0) {
                                        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                        spSubject.setAdapter(customSpinnerAdapter);
                                    } else {
                                        listSubject.clear();
                                        listSubject.add("Subject");
                                        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                        spSubject.setAdapter(customSpinnerAdapter);
                                    }
                                }


                            }


                        } else {

                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        listSubject.clear();
                        listSubject.add("Subject");
                        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                        spSubject.setAdapter(customSpinnerAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
        spSubject.setAdapter(customSpinnerAdapter);

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_subject = spSubject.getSelectedItem().toString();
                if (str_subject.equalsIgnoreCase("Subject")) {

                } else {
                    Log.i("str_subject***", "" + str_subject);
                    strSubject = str_subject;
                    Log.i("str_subject***2", "" + str_subject + strSubject);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_backBtn:
                ExamActivity examActivity = (ExamActivity) getActivity();
                examActivity.loadFragment(Constant.EXAM_BARRIER_LIST, null);
                break;
            case R.id.tvAdd:
                String value = "", editvalue = "";
                IntMaxValue = edMaxMark.getText().toString();
                IntMinValue = edMinMarks.getText().toString();


                if ((strExamType.equalsIgnoreCase("Exam Type")) || (strDiv.equalsIgnoreCase("Division"))
                        || (str_class.equalsIgnoreCase("Class")) || (strSubject.equalsIgnoreCase("Subject"))) {
                    Toast.makeText(getActivity(), "Please select all fields", Toast.LENGTH_SHORT).show();
                } else if (edMinMarks.getText().toString().trim().isEmpty() || edMaxMark.getText().toString().trim().isEmpty() ||
                        edExamDuration.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill  the min marks max marks and exam duration",
                            Toast.LENGTH_SHORT).show();
                }
               else if (Integer.parseInt(IntMaxValue) < Integer.parseInt(IntMinValue)) {
                    Toast.makeText(getActivity(), "Please enter maximum marks greater than minimum marks", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(IntMaxValue) > 100) {
                    Toast.makeText(getActivity(), "Please enter maximum marks less than 100", Toast.LENGTH_SHORT).show();
                } else {


                    long strMin = Long.parseLong(edMinMarks.getText().toString());
                    String strDuration = edExamDuration.getText().toString();
                    long strMax = Long.parseLong(edMaxMark.getText().toString());
                    Log.i("STR value***", "" + strMin + "**" + strMax + "***" + strDuration + "**");

                    if (assesmentModels.size() > 0) {

                        boolean b = true;
                        for (int i = 0; i < assesmentModels.size(); i++) {
                            if ((assesmentModels.get(i).getSubjectName()).contains(strSubject)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();

                            }
                        }
                        if (b) {


                            assesmentModels.add(new AssesmentModel(strMin, strMax, strExamType, strDiv,
                                    str_class, strSubject, strDuration));


                        }

                    } else {
                        assesmentModels.add(new AssesmentModel(strMin, strMax, strExamType, strDiv,
                                str_class, strSubject, strDuration));

                    }


                    addExamBarriers(strMin, strMax, strExamType, strDiv, str_class, strSubject, strDuration);
                    setRecyclerView();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }

                Log.i("STR_assesmentModels***", "" + assesmentModels);

        }
    }

    private void setRecyclerView() {
        //   adapter = new AssessmentAdapter(getActivity(), assesmentModels, Constant.RV_EXAMS_BARRIER_ROW);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  recyclerView.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
    }

    private void addExamBarriers(long strMin, long strMax, String strExamType, String
            strDiv, String str_class, String strSubject, String strDuration) {

        Log.i("strExamType**", "" + strMin + strMax + strExamType + strDiv + str_class + strSubject + strDuration);


        apiService.addExamBarrier(Constant.SCHOOL_ID, strExamType, strDiv, str_class, strSubject, String.valueOf(strMin),
                String.valueOf(strMax), strDuration, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("ADDEXAMBARRIER**", "" + response.body());
                Log.i("ADDEXAMBARRIER**", "" + response.code());
                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        String message = (String) object.get("message");
                        Log.i("SubjectList***D", "" + response.code() + "**" + object.toString());
                        if (message.equalsIgnoreCase("Successfully created exam barrier")) {
                            Toast.makeText(getActivity(), "Successfully created exam barrier", Toast.LENGTH_SHORT).show();

                            ExamActivity examActivity = (ExamActivity) getActivity();
                            examActivity.loadFragment(Constant.EXAM_BARRIER_LIST, null);

                        }

                    } catch (JSONException je) {

                    }


                    spExamType.setSelection(0);
                    spDivision.setSelection(0);
                    spClass.setSelection(0);
                    spSubject.setSelection(0);
                    spn_section.setSelection(0);
                    edExamDuration.setText("");
                    edMaxMark.setText("");
                    edMinMarks.setText("");
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


}










