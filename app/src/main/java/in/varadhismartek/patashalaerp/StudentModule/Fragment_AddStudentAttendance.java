package in.varadhismartek.patashalaerp.StudentModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.zip.DataFormatException;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import in.varadhismartek.patashalaerp.SessionModule.SessionAdapter;
import in.varadhismartek.patashalaerp.SessionModule.SessionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class Fragment_AddStudentAttendance extends Fragment {
    APIService apiService, mApiServicePatashala;
    private ArrayList<String> academicList, sessionList;
    private ArrayList<String> serialList;
    TextView viewTitle, tvTitle2;
    RecyclerView recyclerView;
    ArrayList<String> classList;
    ArrayList<String> divisionList = new ArrayList<>();
    ArrayList<StudentModel> studentAttendanceModels = new ArrayList<>();
    ArrayList<NotificationModel> classSectionList;
    Spinner sp_class, sp_section, sp_acdaemicyear, spn_session;
    String strSpClass, strSpSection;
    ImageView iv_backBtn;
    FloatingActionButton fab;
    StudentRecyclerAdapter studentRecyclerAdapter;
    ArrayList<StudentModel> studentModels = new ArrayList<>();
    String strDivision, strClass, strSection, strFirstName, strLastName, strDOB, strStudentID, strCertificateNo,
            strStatus, strdeleted, strPhoto, strSessionFromDate, strSessionToDate, sessionDate = "";
    String strAcdaemicYear, strSessionYear, model_type = "attendance", currentDate = "";
    String acdaemicFromDate = "", acdaemicToDate = "", sessionFromDate = "", sessionToDate = "", session_serial_no = "";
    String strFromDate, strToDate,endYear, str_toDate;

    Calendar calendar;
    Date date1;
    private int year, month, date, rowNumber = 1;
    public Fragment_AddStudentAttendance() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_attendance_list, container, false);

        initView(view);
        getAllClasses();
        getAcadmicYear();

        return view;
    }


    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        fab = view.findViewById(R.id.fab);
        viewTitle = view.findViewById(R.id.tvTitle);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        sp_acdaemicyear = view.findViewById(R.id.sp_acdaemicyear);
        spn_session = view.findViewById(R.id.spn_session);
        sp_class = view.findViewById(R.id.sp_class);
        sp_section = view.findViewById(R.id.sp_section);
        viewTitle.setText("Add Student Attendance");
        fab.setVisibility(View.GONE);
        classList = new ArrayList<>();
        classSectionList = new ArrayList<>();
        academicList = new ArrayList<>();
        sessionList = new ArrayList<>();
        academicList.clear();
        sessionList.clear();
        spn_session.setVisibility(View.GONE);

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        Calendar calendar = Calendar.getInstance();

         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());
        tvTitle2.setText(currentDate);
        tvTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

    }


    private void openDateDialog() {

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                tvTitle2.setText(str_toDate);
                sp_class.setSelection(0);
                sp_section.setSelection(0);


            }
        }, year, month, date);

        dialog.show();

    }
    private void getAcadmicYear() {
        mApiServicePatashala.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                academicList.clear();
                if (response.isSuccessful()) {

                    try {
                        academicList.add("Academic Year");

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("jwhdlff", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date fromYear = formater.parse(start_date);
                                    Date toYear = formater.parse(end_date);

                                    String from = formatterDate.format(fromYear);
                                    String to = formatterDate.format(toYear);

                                    String acadmicListData = from + " / " + to;
                                    academicList.add(acadmicListData);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            setSpinnerForAcademicYear(academicList);

                        } else {
                            Log.d("ldafhhlka", response.code() + " " + message);
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


        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), academicList);
        sp_acdaemicyear.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_acdaemicyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                strAcdaemicYear = sp_acdaemicyear.getSelectedItem().toString();
                if (strAcdaemicYear.equalsIgnoreCase("Academic Year")) {

                } else {
                    spn_session.setVisibility(View.VISIBLE);
                    String[] date = strAcdaemicYear.split("/");


                    acdaemicFromDate = date[0];
                    acdaemicToDate = date[1];

                    Log.i("ACD***", "" + acdaemicFromDate + acdaemicToDate);

                    SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date inputFromDate = inputFormate.parse(acdaemicFromDate);
                        Date inputToDate = inputFormate.parse(acdaemicToDate);

                        strFromDate = outputFormate.format(inputFromDate);
                        strToDate = outputFormate.format(inputToDate);
                        Log.i("ACD***11", "" + strFromDate + strToDate);

                        getSessionAPI(strFromDate, strToDate);

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
        mApiServicePatashala.getSessions(Constant.SCHOOL_ID, fromDate.trim(), toDate.trim()).
                enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        sessionList.clear();

                        if (response.isSuccessful()) {
                            sessionList.add("Session Year");
                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");

                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonObject1 = object.getJSONObject("data");

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("sessions");

                                    Iterator<String> keys = jsonObject2.keys();
                                    while (keys.hasNext()) {

                                        String key = keys.next();

                                        JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);


                                        strSessionFromDate = jsonObjectValue.getString("from_date");
                                        strSessionToDate = jsonObjectValue.getString("to_date");

                                        sessionDate = strSessionFromDate + " / " + strSessionToDate;
                                        sessionList.add(sessionDate);

                                    }
                                    setSpinnerSessionYear(sessionList);


                                }
                            } catch (JSONException je) {

                            }
                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("create_API", message);
                                sessionList.add("Session Year");
                                // setSpinnerSessionYear(sessionList);
                            } catch (Exception je) {

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    private void setSpinnerSessionYear(ArrayList<String> sessionList) {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), sessionList);
        spn_session.setAdapter(customSpinnerAdapter);

        spn_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSessionYear = spn_session.getSelectedItem().toString();
                Log.i("strSessionYear**", "" + strSessionYear);

                if (strSessionYear.equals("Session Year")) {

                } else {
                    session_serial_no = String.valueOf(spn_session.getItemIdAtPosition(position));

                    String[] sessionDate = strSessionYear.split("/");


                    String strSessionFromDate = sessionDate[0];
                    String strSessionToDate = sessionDate[1];


                    SimpleDateFormat inputFormate = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormate = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        Date inputFromDate = inputFormate.parse(strSessionFromDate);
                        Date inputToDate = inputFormate.parse(strSessionToDate);

                        sessionFromDate = outputFormate.format(inputFromDate);
                        sessionToDate = outputFormate.format(inputToDate);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
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
                classList.add("--Classes--");

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

                                            ArrayList<String> sections = new ArrayList<>();
                                            sections.add("--Sections--");
                                            for (int i = 0; i < jsonArray1.length(); i++) {

                                                sections.add(jsonArray1.getString(i));
                                                Log.d("classData_array " + i, jsonArray1.getString(i));

                                            }

                                            classSectionList.add(new NotificationModel(class_name, sections));
                                        }
                                    }

                                }

                                CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), classList);
                                sp_class.setAdapter(customSpinnerAdapter);

                                sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                        strSpClass = parent.getItemAtPosition(position).toString();
                                        Log.d("strSpClass ","" + strSpClass);



                                        String sec="";
                                        ArrayList<String> listSection = new ArrayList<>();

                                        if (!strSpClass.equals("--Classes--")) {

                                            for (int i = 0; i < classSectionList.size(); i++) {

                                                if (strSpClass.contains(classSectionList.get(i).getClassName())) {
                                                    Log.d("loopI", ""+i);

                                                    listSection.clear();
                                                    for (int j = 0; j < classSectionList.get(i).getSectionName().size(); j++) {
                                                        sec = classSectionList.get(i).getSectionName().get(j);
                                                        listSection.add(sec);
                                                        Log.d("loopJ", ""+j);

                                                    }



                                                    System.out.println("classSectionList*** Sec" + sec);

                                                }
                                            }
                                        } else {
                                            listSection.add("--Sections--");
                                        }


                                        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(),
                                                listSection);
                                        sp_section.setAdapter(customSpinnerAdapter1);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                                sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        strSpSection = parent.getItemAtPosition(position).toString();
                                        Log.i("Sections**", "" + strSpSection);
                                        if ((strAcdaemicYear.equalsIgnoreCase("Academic Year")) ||
                                                (strSessionYear.equalsIgnoreCase("Session Year")) ||
                                                (strSpClass.equalsIgnoreCase("--Classes--")) ||
                                                strSpSection.equalsIgnoreCase("--Sections--")) {

                                            Toast.makeText(getActivity(), "Select all fields", Toast.LENGTH_SHORT).show();


                                        } else {
                                            studentListByClassSection(strSpClass, strSpSection);

                                        }


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

    private void studentListByClassSection(String strSpClass, String strSpSection) {
        apiService.getStudentAllByClassSection(Constant.SCHOOL_ID, strSpClass, strSpSection)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("studentList_byclassaa**", "" + response.body() + "**" + response.code());
                        if (response.isSuccessful()) {

                            try {
                                studentModels.clear();
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                System.out.println("MEssage**C*" + object.getString("message"));
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    Iterator<String> keys = jsonObject.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject objectData = jsonObject.getJSONObject(key);


                                        strDivision = objectData.getString("division");
                                        strClass = objectData.getString("class");
                                        strSection = objectData.getString("section");
                                        strFirstName = objectData.getString("first_name");
                                        strLastName = objectData.getString("last_name");
                                        strDOB = objectData.getString("student_dob");
                                        strStudentID = objectData.getString("student_id");
                                        strCertificateNo = objectData.getString("birth_certificate_no");
                                        strStatus = objectData.getString("status");
                                        strdeleted = objectData.getString("student_deleted");
                                        strPhoto = objectData.getString("photo");

                                        studentModels.add(new StudentModel(strDivision, strClass, strSection, strFirstName, strLastName,
                                                strDOB, strStudentID, strCertificateNo, tvTitle2.getText().toString().trim(), strStatus, strdeleted, strPhoto,
                                                acdaemicFromDate, acdaemicToDate, sessionFromDate, sessionToDate,
                                                session_serial_no, model_type));

                                    }
                                    if (studentModels.size() > 0) {
                                        setRecyclerView(studentModels);
                                    } else {

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
                                studentModels.clear();
                                //setRecyclerView(studentModels);

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

    private void setRecyclerView(ArrayList<StudentModel> studentModels) {
        studentRecyclerAdapter = new StudentRecyclerAdapter(studentModels, getActivity(), Constant.RV_ADD_STUDENT_ATTENDANCE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(studentRecyclerAdapter);
        studentRecyclerAdapter.notifyDataSetChanged();
    }
}
