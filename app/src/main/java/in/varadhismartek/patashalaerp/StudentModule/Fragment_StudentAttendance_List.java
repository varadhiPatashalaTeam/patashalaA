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

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_StudentAttendance_List extends Fragment {
    APIService apiService;
    TextView viewTitle, tvTitle2;
    RecyclerView recyclerView;

    StudentRecyclerAdapter attendanceAdapter;
    ArrayList<String> classList;
    ArrayList<String> divisionList = new ArrayList<>();
    ArrayList<StudentModel> studentAttendanceModels = new ArrayList<>();
    ArrayList<NotificationModel> classSectionList;
    Spinner sp_class, sp_section;
    String strSpClass, strSpSection, endYear, str_toDate;
    ImageView iv_backBtn;
    FloatingActionButton fab;
    private int year, month, date, rowNumber = 1;

    String added_by_emp_firstname, session_to, session, added_by, student_last_name, student_id,
            student_birth_certificate_number, student_first_name,
            attStatus, student_dob, added_datetime, session_from,
            student_attendance_id, photo, added_by_emp_lastname, updated_datetime;

    Calendar calendar;
    Date date1;
    TextView noRecords;

    public Fragment_StudentAttendance_List() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_all_list, container, false);

        initView(view);
        getAllClasses();
        //studentAll();
        return view;
    }

    @SuppressLint("WrongConstant")
    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        fab = view.findViewById(R.id.fab);
        viewTitle = view.findViewById(R.id.tvTitle);
        noRecords = view.findViewById(R.id.noRecords);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        sp_class = view.findViewById(R.id.sp_class);
        sp_section = view.findViewById(R.id.sp_section);
        viewTitle.setText("Student Attendance List");

        classList = new ArrayList<>();
        classSectionList = new ArrayList<>();

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentModuleActivity studentModuleActivity = (StudentModuleActivity) getActivity();
                studentModuleActivity.loadFragment(Constant.ADD_STUDENT_ATTENDANCE, null);


            }
        });

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDate.format(calendar.getTime());
        tvTitle2.setText(currentDate);

        tvTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDateDialog();

            }
        });
        if (studentAttendanceModels.size()==0){
            noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

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

      //  dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
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

                                            Gson g = new Gson();
                                            Log.i("classList**",""+g.toJson(classList));

                                            JSONArray jsonArray1 = classData.getJSONArray("sections");

                                            ArrayList<String> sections = new ArrayList<>();
                                            sections.clear();
                                            sections.add("--Sections--");

                                            for (int i = 0; i < jsonArray1.length(); i++) {

                                                sections.add(jsonArray1.getString(i));
                                                Log.d("classData_array " + i, jsonArray1.getString(i));

                                            }

                                            classSectionList.add(new NotificationModel(class_name, sections));



                                        }
                                        Gson g1 = new Gson();
                                        Log.i("classSectionList**",""+g1.toJson(classSectionList));
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
                                        //Log.i("Sections**", "" + strSpSection);
                                        if (!strSpSection.equalsIgnoreCase("--Sections--")) {
                                            studentAttendanceList(strSpClass, strSpSection);
                                            Log.i("Sections**", ""+strSpClass + strSpSection);

                                            // studentListByClassSection(strSpClass, strSpSection);
                                        }
                                        //

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

    private void studentAttendanceList(String strSpClass, String strSpSection) {
//"2019-06-07"
        Log.d("myStuAttendance_data", "" + strSpSection +strSpClass+ "**" + tvTitle2.getText().toString().trim());
        apiService.getStudentAttendanceClassBy(Constant.SCHOOL_ID, strSpClass,
                strSpSection, tvTitle2.getText().toString().trim())
                .enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d("myStuAttendance_data22", "" + response.code() + "**" + response.body());
                        studentAttendanceModels.clear();
                        if (response.isSuccessful()) {

                            try {

                                JSONObject obj = null;

                                obj = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("myStuAttendance_data2", String.valueOf(response.code()));

                                String status = obj.getString("status");
                                Log.d("myStuAttendance_dat3", obj.toString());


                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonData = obj.getJSONObject("data");
                                    Log.d("myStuAttendance_dat4", jsonData.toString());

                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()) {
                                        String stuAttKey = key.next();

                                        JSONObject keyData = jsonData.getJSONObject(stuAttKey);


                                        added_by_emp_firstname = keyData.getString("added_by_emp_firstname");
                                        session_to = keyData.getString("session_to");
                                        session = keyData.getString("session");
                                        added_by = keyData.getString("added_by");
                                        student_last_name = keyData.getString("student_last_name");
                                        student_id = keyData.getString("student_id");
                                        student_birth_certificate_number = keyData.getString("student_birth_certificate_number");
                                        student_first_name = keyData.getString("student_first_name");
                                        attStatus = keyData.getString("status");
                                        student_dob = keyData.getString("student_dob");
                                        added_datetime = keyData.getString("added_datetime");
                                        session_from = keyData.getString("session_from");
                                        student_attendance_id = keyData.getString("student_attendance_id");
                                        added_by_emp_lastname = keyData.getString("added_by_emp_lastname");

                                        if (keyData.isNull("photo")) {
                                            photo = "";
                                        } else {
                                            photo = keyData.getString("photo");
                                        }

                                        if (keyData.isNull("updated_datetime")) {
                                            updated_datetime = "";

                                        } else {
                                            updated_datetime = keyData.getString("updated_datetime");

                                        }


                                        Log.i("_emp_firstname**", "" + added_by_emp_firstname);

                                        studentAttendanceModels.add(new StudentModel(added_by_emp_firstname, session_to, session, added_by, student_last_name,
                                                student_id, student_birth_certificate_number, student_first_name, attStatus, student_dob, added_datetime,
                                                session_from, student_attendance_id, photo, added_by_emp_lastname, updated_datetime));

                                    }
                                  setRecyclerView(studentAttendanceModels);
                                }
                            } catch (JSONException je) {
                                je.printStackTrace();
                                Log.d("messageAtt", je.getMessage());
                                setRecyclerView(studentAttendanceModels);


                            }

                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("create_API", message);
                                setRecyclerView(studentAttendanceModels);

                                //studentAttendanceModels.clear();
                               // setRecyclerView(studentAttendanceModels);
                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

        /*if (studentAttendanceModels.size()>0){
            noRecords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            setRecyclerView(studentAttendanceModels);
        }else {
            noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }*/

    }

    private void setRecyclerView(ArrayList<StudentModel> studentAttendanceModels) {

        if (studentAttendanceModels.size()>0){
            noRecords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            attendanceAdapter = new StudentRecyclerAdapter(Constant.RV_STUDENT_ATTENDANCE_LIST, getActivity(), studentAttendanceModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(attendanceAdapter);
            attendanceAdapter.notifyDataSetChanged();
        }else {
            noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

}
