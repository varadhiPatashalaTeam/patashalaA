package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
import in.varadhismartek.patashalaerp.ClassAndSection.ClassSectionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class Fragment_AddEvent extends Fragment implements View.OnClickListener {

    private TextView tvTitle;
    private EditText edAddExam;
    private ImageView ivAddExam, ivSendExam, ivBack;
    private RecyclerView rcvAddWings;
    APIService apiService;
    private ArrayList<AddWingsModel> addExamArrayList;
    private ArrayList<String> examList = new ArrayList<>();
    AssessmentAdapter assessmentAdapter;
    int spanCount = 2;
    private Button buttonAdd;
    String editvalue;


    APIService  mApiServicePatashala;
    private ArrayList<String> academicList, listDivision, listClass, listSection, sessionList, listSectionNew,
            listscheduleType;
    private ArrayList<String> serialList;
    private DateConvertor convertor;
    private Utilities utilities;
    Spinner sp_academic, spn_division, spn_class, spn_section, sp_sessionNo, spn_schedule_type;
    String strAcdaemicYear, acdaemicFromDate, acdaemicToDate, strFromDate, strToDate, strScheduleType;
    private String strDiv, strSection, str_class;
    String fromYear, toYear, strfromDate, str_toDate;
    String serialNo = "";
    String fromDate = "";
    String toDate = "";
    String strAcademicFDate,strAcademicTDate;
    ArrayList<ClassSectionModel> modelArrayList;
    public Fragment_AddEvent() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViews(view);
        initListener();

        getAcademicYearAPI();
        getDivisionApi();
       // getExamType();
        return view;

    }

    private void initListener() {
        ivAddExam.setOnClickListener(this);
        ivSendExam.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

    }

    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();

        tvTitle = view.findViewById(R.id.tvTitle);
        edAddExam = view.findViewById(R.id.etAddWings);
        ivBack = view.findViewById(R.id.iv_backBtn);
        buttonAdd = view.findViewById(R.id.button_added);

        ivAddExam = view.findViewById(R.id.ivAddWings);
        ivSendExam = view.findViewById(R.id.iv_sendAddWings);
        rcvAddWings = view.findViewById(R.id.rcvAddWings);


        tvTitle.setText("Add Event");
        edAddExam.setHint("Add Event");

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        rcvAddWings.setLayoutManager(linearLayoutManager);
        rcvAddWings.setHasFixedSize(true);
        addExamArrayList = new ArrayList<>();

        sp_academic = view.findViewById(R.id.sp_academic);
        sp_sessionNo = view.findViewById(R.id.sp_sessionNo);
        spn_division = view.findViewById(R.id.spn_division);
        spn_class = view.findViewById(R.id.spn_class);
        spn_section = view.findViewById(R.id.spn_section);
        spn_schedule_type = view.findViewById(R.id.spn_schedule_type);
        academicList = new ArrayList<>();
        serialList = new ArrayList<>();
        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();
        listSectionNew = new ArrayList<>();
        modelArrayList = new ArrayList<>();
        listscheduleType = new ArrayList<>();
        convertor = new DateConvertor();

    }



    private void getAcademicYearAPI() {
        Log.d("jwhdlff", ";jlfjdgfj;");

        if (academicList.size() > 0) {
            academicList.clear();
        }

        academicList.add(0, "-Academic Year-");

        mApiServicePatashala.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("AcademicYear", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                Log.i("AcademicYear_11", start_date + " " + end_date);

                                //String from = convertor.getDateByTZ(start_date);
                                //String to = convertor.getDateByTZ(end_date);

                                Log.i("AcademicYear_12", start_date + " " + end_date);

                                academicList.add(start_date + "/" + end_date);
                                Log.i("AcademicYear_2", "" + academicList.size());
                            }
                            Log.i("lakfblkasAAAA", "" + academicList.size());


                            setSpinnerForAcademicYear(academicList);

                        } else {
                            Log.d("AcademicYear_3", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
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

    private void setSpinnerForAcademicYear(ArrayList<String> academicList) {
        Log.d("asflkaf1", "kgdkdsgkgd");
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), academicList);
        sp_academic.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_academic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("asflkaf2", "kgdkdsgkgd" + i);


                if (i == 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(0, "-Session-");
                    setSpinnerForSerialNo(list, "", "");

                } else {

                    strAcdaemicYear = sp_academic.getSelectedItem().toString();
                    String[] date = strAcdaemicYear.split("/");

                    fromDate = date[0];
                    toDate = date[1];

                    Log.d("asflkaf", "kgdkdsgkgd"+fromDate+toDate);
                    getSessionAPI(fromDate, toDate);


                    SimpleDateFormat inputFormate = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date inputFromDate = inputFormate.parse(fromDate);
                        Date inputToDate = inputFormate.parse(toDate);

                        strAcademicFDate = outputFormate.format(inputFromDate);
                        strAcademicTDate = outputFormate.format(inputToDate);

                        Log.i("strAcademicFDate",""+strAcademicFDate);
                        Log.i("strAcademicTDate",""+strAcademicTDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getSessionAPI(final String fromDate, final String toDate) {

        if (serialList.size() > 0) {
            serialList.clear();
        }

        serialList.add(0, "-Session-");

        mApiServicePatashala.getSessions(Constant.SCHOOL_ID, fromDate, toDate).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");

                            JSONObject sessionsData = jsonData.getJSONObject("sessions");

                            Iterator<String> key = sessionsData.keys();

                            while (key.hasNext()) {

                                JSONObject keyData = sessionsData.getJSONObject(key.next());

                                int session_serial_no = keyData.getInt("session_serial_no");
                                Log.d("sessionSerialNO", String.valueOf(session_serial_no));

                                serialList.add(session_serial_no + "");
                            }

                            setSpinnerForSerialNo(serialList, fromDate, toDate);

                        } else {
                            Log.d("ldafhhlkafhd", response.code() + " " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API2", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void setSpinnerForSerialNo(ArrayList<String> list, final String fromDate, final String toDate) {

        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(getActivity(), list, "Green");
        sp_sessionNo.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        sp_sessionNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (i == 0) {


                } else {

                    serialNo = sp_sessionNo.getSelectedItem().toString();
                    Log.d("slhdlhsf", serialNo);

                    String from = convertor.getDateDMY_to_YMD(fromDate);
                    String to = convertor.getDateDMY_to_YMD(toDate);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
        spn_division.setAdapter(customSpinnerAdapter);

        spn_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (i == 0) {
                    listscheduleType.add("Schedule Type");


                } else {

                    strDiv = spn_division.getSelectedItem().toString();

                    listscheduleType.clear();
                    listscheduleType.add("Schedule Type");
                    listscheduleType.add("Exam");
                    listscheduleType.add("Holiday");
                    listscheduleType.add("Event");


                }

                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listscheduleType);
                spn_schedule_type.setAdapter(customSpinnerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    private void getExamType() {
        addExamArrayList.clear();
        Log.i("Exam_getlist *",""+"DATA"+ Constant.SCHOOL_ID);
        apiService.getExamList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Exam_getlist *", "" + response.body());

                if (response.isSuccessful()) {
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

                                // if (exam_status) {

                                addExamArrayList.add(new AddWingsModel(exam_name, exam_status));
                                assessmentAdapter = new AssessmentAdapter(addExamArrayList,
                                        getActivity(), ivSendExam, Constant.RV_EXAMS_ROW);
                                rcvAddWings.setAdapter(assessmentAdapter);
                                // }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddWings:

                String value = "";
                editvalue = edAddExam.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(getActivity(), "Please enter the Value", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }else {
                    if (addExamArrayList.size()>0){
                        for (int i = 0; i < addExamArrayList.size(); i++) {
                            if (addExamArrayList.get(i).getWingsName().toLowerCase().equals(editvalue.toLowerCase())) {
                                value = addExamArrayList.get(i).getWingsName();
                            }
                        }

                        if (value.toLowerCase().equals(editvalue.toLowerCase())) {
                            Toast.makeText(getActivity(), "Already Exist", LENGTH_SHORT).show();
                        } else {
                            assessmentAdapter.newValues(editvalue);
                            examList.add(editvalue);
                            System.out.println("addExamArrayList**11  "+examList);

                        }

                        edAddExam.setText("");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    else{

                        assessmentAdapter = new AssessmentAdapter(addExamArrayList,
                                getActivity(), ivSendExam, Constant.RV_EXAMS_ROW);
                        rcvAddWings.setAdapter(assessmentAdapter);
                        assessmentAdapter.newValues(editvalue);
                        edAddExam.setText("");
                        examList.add(editvalue);


                    }
                }

                break;
            case R.id.button_added:
                System.out.println("addExamArrayList**"+addExamArrayList.size()+"**"+editvalue+"**"+examList);

                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < examList.size(); i++) {
                    array.put(examList.get(i));

                }

                try {
                    jsonObject.put("exam_type", array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (addExamArrayList.size()>0){
                    addEventAPIMethod(array);
                }


                System.out.println("objExam***"+array);
                System.out.println("objExam***"+jsonObject);






                break;
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }

    }


    private void addEventAPIMethod(JSONArray array) {
        apiService.addExamType(Constant.SCHOOL_ID,array,Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("Exam_getlist *", "" + response.raw().request().url());
                        Log.i("Exam_getlist_R *", "" + response.body());
                        Log.i("Exam_getlist *", "" + response.code());
                        Log.i("Exam_getlist *", "" + Constant.SCHOOL_ID);
                        Log.i("Exam_getlist *", "" + Constant.EMPLOYEE_BY_ID);
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(),"Data have save successfully", LENGTH_SHORT).show();
                            getExamType();
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }
}
