package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.DashboardModule.NotificationModule.NotificationModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_HomeworkInbox extends Fragment implements View.OnClickListener {


    String strDiv = "", str_class = "", str_toDate = "", endYear = "", currentDate = "", strSubject = "";

    HomeworkAdapter homeworkAdapter;
    APIService mApiService;
    LinearLayout linearLayoutDate;
    Calendar calendar;
    Date date1;

    private int year, month, date;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView tvFromDate;
    ImageView iv_backBtn;
    Spinner spDivision, spnClass, spnSection, spnSubject, spnTeacher;
    RecyclerView recycler_view;
    FloatingActionButton fab;

    ArrayList<HomeworkModel> homeworkModelsInbox;
    ArrayList<HomeworkModel> homeworkModelsInboxDate;
    ArrayList<String> listDivision;
    ArrayList<String> listClass;
    ArrayList<String> listSection;
    ArrayList<String> listSubject;
    String homeworkId, homeworkTitle, description, homeClass, section, subject, startDate, endDate;

    private ArrayList<NotificationModel> classSectionList;

    ArrayList<String> employeeNameList = new ArrayList<>();

    public Fragment_HomeworkInbox() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__homework_inbox, container, false);


        initViews(view);
        initListeners();
        getDivisionApi();
        getSchoolHomework();

        // getTeacherList();

        //   getAllClasses();

        return view;
    }

    private void initListeners() {
        fab.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        spDivision = view.findViewById(R.id.sp_division);
        spnClass = view.findViewById(R.id.sp_class);
        spnSection = view.findViewById(R.id.sp_section);
        spnSubject = view.findViewById(R.id.sp_subject);
        spnTeacher = view.findViewById(R.id.sp_teacher);
        linearLayoutDate = view.findViewById(R.id.ll_date);

        tvFromDate = view.findViewById(R.id.tv_fromDate);
        recycler_view = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);

        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();

        listSubject = new ArrayList<>();
        classSectionList = new ArrayList<>();
        homeworkModelsInbox = new ArrayList<>();
        homeworkModelsInboxDate = new ArrayList<>();
        spnSection.setVisibility(View.GONE);
        spnTeacher.setVisibility(View.GONE);
        spnSubject.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());

        tvFromDate.setText(currentDate);
    }


    private void getDivisionApi() {

        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                listDivision.clear();
                listDivision.add(0, "-Division-");

                boolean statusDivision = false;
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                statusDivision = object1.getBoolean("status");
                                if (statusDivision) {
                                    String division = object1.getString("division");

                                    listDivision.add(division);
                                }


                            }
                            customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listDivision);
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
                Log.d("GET_DIVISION_EXCEPTION", t.toString());

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strDiv = parent.getSelectedItem().toString();
                System.out.println("strDiv**" + strDiv);
                //  setSpinnerForClass();

                spnSection.setVisibility(View.GONE);
                spnTeacher.setVisibility(View.GONE);
                spnSubject.setVisibility(View.GONE);

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
        listClass.add(0, "-Class-");
        mApiService.getClassSections(Constant.SCHOOL_ID, jsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (response.isSuccessful()) {
                    Log.i("CLASS_SECTIONDDD", "" + response.body() + "***" + response.code());
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            if (object.getJSONObject("data").toString().equals("{}")) {
                                //modelArrayList.clear();
                                // customSpinnerAdapter.notifyDataSetChanged();
                                //   setSpinnerForClass();

                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("data");
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

                                    classSectionList.add(new NotificationModel(className, listSection));

                                    listClass.add(className);
                                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                                    spnClass.setAdapter(customSpinnerAdapter);

                                }
                            }


                        }


                        //  setRecyclerView();

                    } catch (JSONException je) {

                    }

                } else {

                    listClass.clear();
                    listClass.add("-Class-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listClass);
                    spnClass.setAdapter(customSpinnerAdapter);
                    customSpinnerAdapter.notifyDataSetChanged();

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
                if (str_class.equalsIgnoreCase("-Class-")) {

                } else {

                    ArrayList<String> listSection = new ArrayList<>();
                    listSection.clear();
                    listSubject.clear();
                    listSection.add("-Section-");
                    listSubject.add("-Subject-");
                    spnSection.setVisibility(View.GONE);
                    spnTeacher.setVisibility(View.GONE);
                    spnSubject.setVisibility(View.VISIBLE);

                    // str_class = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < classSectionList.size(); i++) {
                        if (str_class.contains(classSectionList.get(i).getClassName())) {
                            for (int j = 0; j < classSectionList.get(i).getSectionName().size(); j++) {
                                String strScetion = classSectionList.get(i).getSectionName().get(j);
                                System.out.println("strScetion***" + strScetion);

                                listSection.add(strScetion);

                            }
                        }
                    }

                    CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(), listSection);
                    spnSection.setAdapter(customSpinnerAdapter1);

                    homeworkModelsInbox.clear();
                    homeworkModelsInboxDate.clear();

                    getSubject(strDiv, str_class);

                    getHomeWorkList(str_class);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getSubject(String strDiv, final String str_class) {
        mApiService.getSubject(Constant.SCHOOL_ID, strDiv, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                Log.i("SubjectList***Data", "" + response.code() + "**" + response.body());
                if (response.isSuccessful()) {
                    try {

                        HashSet<String> hashSet = new HashSet<String>();

                        listSubject.clear();


                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        Log.i("SubjectList***D", "" + response.code() + "**" + status);

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = object.getJSONObject("Section");

                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String sectionkey = keys.next();
                                JSONObject jsonSection = jsonObject1.getJSONObject(sectionkey);

                                System.out.println("sectionkey**"+sectionkey);
                                System.out.println("sectionkey**"+jsonSection.toString());

                                if (jsonSection.toString().equals("{}")){
                                    listSubject.clear();
                                    listSubject.add("-Subject-");

                                }else {

                                    Iterator<String> keys2 = jsonSection.keys();

                                    while (keys2.hasNext()) {
                                        String subjectkey = keys2.next();
                                        JSONObject jsonSubject = jsonSection.getJSONObject(subjectkey);

                                        listSubject.add(subjectkey);


                                    }

                                    hashSet.addAll(listSubject);
                                    listSubject.clear();
                                    listSubject.addAll(hashSet);




                                }
                                customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                                spnSubject.setAdapter(customSpinnerAdapter);


                                //  setRecyclerView();
                            }


                        }

                    } catch (JSONException je) {

                    }
                } else {

                    Log.i("SubjectList***F", "" + response.code());
                    listSubject.clear();
                    listSubject.add("-Subject-");
                    customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), listSubject);
                    spnSubject.setAdapter(customSpinnerAdapter);
                }

            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        spnSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSubject = spnSubject.getItemAtPosition(position).toString();
                System.out.println("Subject***" + strSubject);
                if (strSubject != null) {
                    System.out.println("Subject***1" + strSubject);
                       getHWClassByDate(str_class);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getSchoolHomework() {

        mApiService.getHomeWorkBySchool(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        Log.i("homeinboxSChool**", "" + response.body() + "*****" + response.code());

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("datadict");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                if (objectData.isNull("homework_uuid")) {
                                    homeworkId = "";
                                } else {
                                    homeworkId = objectData.getString("homework_uuid");
                                }

                                if (objectData.isNull("homework_title")) {
                                    homeworkTitle = "";
                                } else {
                                    homeworkTitle = objectData.getString("homework_title");
                                }
                                if (objectData.isNull("description")) {
                                    description = "";
                                } else {
                                    description = objectData.getString("description");
                                }


                                if (objectData.isNull("class")) {
                                    homeClass = "";
                                } else {
                                    homeClass = objectData.getString("class");
                                }
                                if (objectData.isNull("section")) {
                                    section = "";
                                } else {
                                    section = objectData.getString("section");
                                }
                                if (objectData.isNull("subject")) {
                                    subject = "";
                                } else {
                                    subject = objectData.getString("subject");
                                }
                                if (objectData.isNull("start_date")) {
                                    startDate = "";
                                } else {
                                    startDate = objectData.getString("start_date");
                                }
                                if (objectData.isNull("end_date")) {
                                    endDate = "";
                                } else {
                                    endDate = objectData.getString("end_date");
                                }


                                homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                        homeClass, section, subject, startDate, endDate));

                                Log.i("homeworkId**", "" + homeworkId + homeworkTitle);


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

    private void getHomeWorkList(final String str_class) {
        homeworkModelsInbox.clear();

        mApiService.getHomeWorkByClass(Constant.SCHOOL_ID, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("homeinbox**Class", "" + response.body() + "*****" + response.code());
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                if (objectData.isNull("homework_uuid")) {
                                    homeworkId = "";
                                } else {
                                    homeworkId = objectData.getString("homework_uuid");
                                }

                                if (objectData.isNull("homework_title")) {
                                    homeworkTitle = "";
                                } else {
                                    homeworkTitle = objectData.getString("homework_title");
                                }
                                if (objectData.isNull("description")) {
                                    description = "";
                                } else {
                                    description = objectData.getString("description");
                                }


                                if (objectData.isNull("class")) {
                                    homeClass = "";
                                } else {
                                    homeClass = objectData.getString("class");
                                }
                                if (objectData.isNull("section")) {
                                    section = "";
                                } else {
                                    section = objectData.getString("section");
                                }
                                if (objectData.isNull("subject")) {
                                    subject = "";
                                } else {
                                    subject = objectData.getString("subject");
                                }
                                if (objectData.isNull("start_date")) {
                                    startDate = "";
                                } else {
                                    startDate = objectData.getString("start_date");
                                }
                                if (objectData.isNull("end_date")) {
                                    endDate = "";
                                } else {
                                    endDate = objectData.getString("end_date");
                                }
                                Log.i("Sele_Subject",""+subject+"**"+strSubject+str_class+tvFromDate.getText().toString());



                                    homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                            homeClass, section, subject, startDate, endDate));


                            }


                            setRecyclerView();
                        }

                    } catch (JSONException je) {

                    }

                } else {
                    Log.i("homeinbox**A", "" + response.body() + "*****" + response.code());
                    homeworkModelsInboxDate.clear();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


    private void getTeacherList() {
        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        //  employeeList.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("EMPLKEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("EMPKEYDATA", keyData.toString());

                               /* sex = keyData.getString("sex");
                                employee_status = keyData.getString("employee_status");
                                wing_name = keyData.getString("wing_name");
                                employee_photo = keyData.getString("employee_photo");
                                role = keyData.getString("role");

                                empUUId = keyData.getString("employee_uuid");
                                empFname = keyData.getString("first_name");
                                empLname = keyData.getString("last_name");
                                empPhoneNo = keyData.getLong("phone_number") + "";
                                empAdhaarNo = keyData.getString("adhaar_card_no");
                                empDept = keyData.getString("department_name");
                                Approved */
                                String employee_status = keyData.getString("employee_status");

                                String empFname = keyData.getString("first_name");
                                String empLname = keyData.getString("last_name");
                                //  Log.d("empPhoneNo", empPhoneNo);
                                String empName = empFname + " " + empLname;
                                if (employee_status.equalsIgnoreCase("Approved")) {
                                    employeeNameList.add(empName);
                                }

                              /*  employeeList.add(new EmpLeaveModel(empFname, empLname, empUUId, empPhoneNo, empAdhaarNo, empDept
                                        , employee_status, wing_name, employee_photo, role, sex));*/
                            }

                            // setRecylerView();
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), employeeNameList);
                            spnTeacher.setAdapter(customSpinnerAdapter);

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

    private void setRecyclerView() {

        System.out.println("homeworkModelsInbox**" + homeworkModelsInbox.size());
        homeworkAdapter = new HomeworkAdapter(homeworkModelsInbox, getActivity(),
                tvFromDate.getText().toString(), Constant.RV_HOMEWORK_INBOX_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(homeworkAdapter);
        homeworkAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {

        DashBoardMenuActivity homeWorkActivity = (DashBoardMenuActivity) getActivity();

        switch (view.getId()) {

            case R.id.tv_fromDate:
                openDateDialog();
                break;
            case R.id.fab:

                homeWorkActivity.loadFragment(Constant.HOMEWORK_CREATE_FRAGMENT, null);
                break;

            case R.id.iv_backBtn:
                getActivity().finish();
                break;
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

                tvFromDate.setText(str_toDate);
                getHWClassByDate(str_class);


            }
        }, year, month, date);

        // dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();

    }

    private void getHWClassByDate(String str_class) {

        homeworkModelsInbox.clear();
        homeworkModelsInboxDate.clear();

        mApiService.getHomeWorkByClass(Constant.SCHOOL_ID, str_class).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("homeinboxDate****", "" + response.body() + "*****" + response.code());
                if (response.isSuccessful()) {
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectData = jsonArray.getJSONObject(i);


                                if (objectData.isNull("homework_uuid")) {
                                    homeworkId = "";
                                } else {
                                    homeworkId = objectData.getString("homework_uuid");
                                }

                                if (objectData.isNull("homework_title")) {
                                    homeworkTitle = "";
                                } else {
                                    homeworkTitle = objectData.getString("homework_title");
                                }
                                if (objectData.isNull("description")) {
                                    description = "";
                                } else {
                                    description = objectData.getString("description");
                                }


                                if (objectData.isNull("class")) {
                                    homeClass = "";
                                } else {
                                    homeClass = objectData.getString("class");
                                }
                                if (objectData.isNull("section")) {
                                    section = "";
                                } else {
                                    section = objectData.getString("section");
                                }
                                if (objectData.isNull("subject")) {
                                    subject = "";
                                } else {
                                    subject = objectData.getString("subject");
                                }
                                if (objectData.isNull("start_date")) {
                                    startDate = "";
                                } else {
                                    startDate = objectData.getString("start_date");
                                }
                                if (objectData.isNull("end_date")) {
                                    endDate = "";
                                } else {
                                    endDate = objectData.getString("end_date");
                                }


                                if ((startDate.equals(tvFromDate.getText().toString())) && (strSubject.contains(subject))) {

                                    homeworkModelsInboxDate.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                            homeClass, section, subject, startDate, endDate));
                                }


                            }

                            homeworkAdapter = new HomeworkAdapter(homeworkModelsInboxDate, getActivity(),
                                    tvFromDate.getText().toString(), Constant.RV_HOMEWORK_INBOX_ROW);

                            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recycler_view.setAdapter(homeworkAdapter);
                            homeworkAdapter.notifyDataSetChanged();

                            //  setRecyclerView();
                        }

                    } catch (JSONException je) {

                    }

                } else {
                    Log.i("homeinbox**B", "" + response.body() + "*****" + response.code());
                    homeworkModelsInboxDate.clear();
                    homeworkAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


}
