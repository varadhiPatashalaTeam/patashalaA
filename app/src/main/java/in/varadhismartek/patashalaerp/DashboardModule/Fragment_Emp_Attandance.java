package in.varadhismartek.patashalaerp.DashboardModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import in.varadhismartek.patashalaerp.AddDepartment.Data;
import in.varadhismartek.patashalaerp.DashboardModule.Attendance.AttendanceAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.Attendance.EmpAttendanceModel;
import in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule.EmployeeAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.EmpLeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Emp_Attandance extends Fragment implements View.OnClickListener {
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<EmpLeaveModel> employeeList, newEmployeeList;
    Spinner spn_employee;
    APIService mApiService;
    RecyclerView recycler_view;
    AttendanceAdapter attendanceAdapter;
    String empFname = "", empLname = "", empUUId, empPhoneNo, empAdhaarNo, empDept, custom_EMPID;
    String fragmentType = "", employee_status, wing_name, employee_photo, role, sex;
    TextView tvTitle, tvTitle2;
    ArrayList<String> arrayListDept = new ArrayList<>();
    ArrayList<String> arrayListRoles = new ArrayList<>();
    ArrayList<Data> list = new ArrayList<>();
    Spinner sp_dept, sp_roles;
    JSONObject wingsJsonObject = new JSONObject();
    String deptValue, strRoles;
    private int year, month, date, rowNumber = 1;
    private String str_sessionName = "", strSelectSession = "", str_from_date, str_toDate,
            startYear = "", endYear = "", sDate = "", eDate = "";
    EmployeeAdapter adapter;
    ImageView iv_backBtn;

    String  employee_name="",empName="", logOut_time, empStatus, total_time, logIn_time, attendance_id, employee_id, employee_image,
            employee_department = "", currentDate;
    ArrayList<EmpAttendanceModel> empAttendanceModels = new ArrayList<>();

    public Fragment_Emp_Attandance() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_att_employee, container, false);

        initViews(view);

        getDepartment();
        getEmployeeAttendance();

        // getRoles();
        //  getAllEmployeeAPI();
        return view;
    }


    //private void fileter(String deptValue, String strRoles) {
    private void fileter(String strRoles) {
        if (employeeList.size() > 0) {
            newEmployeeList.clear();
            for (int i = 0; i < employeeList.size(); i++) {
                if (strRoles.contains(employeeList.get(i).getEmpDept()) || (strRoles.contains(employeeList.get(i).getRole()))) {
                    String empName = employeeList.get(i).getEmpFname();
                    Log.i("empName**", empName);

                    newEmployeeList.add(new EmpLeaveModel(
                            employeeList.get(i).getCustom_EMPID(),
                            employeeList.get(i).getEmpFname(),
                            employeeList.get(i).getEmpLname(),
                            employeeList.get(i).getEmpUUId(),
                            employeeList.get(i).getEmpPhoneNo(),
                            employeeList.get(i).getEmpAdhaarNo(),
                            employeeList.get(i).getEmpDept(),
                            employeeList.get(i).getEmployee_status(),
                            employeeList.get(i).getWing_name(),
                            employeeList.get(i).getEmployee_photo(),
                            employeeList.get(i).getRole(),
                            employeeList.get(i).getSex()

                    ));
                }
            }
            setRecylerViewFilter(newEmployeeList);

        }

    }


    private void getDepartment() {
        try {
            JSONArray wingsArray = new JSONArray();
            Constant.DEPART_NAME = "departments";
            wingsArray.put("All");
            wingsJsonObject.put("wings", wingsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        arrayListDept = new ArrayList<>();
        arrayListDept.clear();
        arrayListDept.add(0, "Department");
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
                                boolean dept_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (dept_status) {
                                    arrayListDept.add(wing_name);
                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));

                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Role ERROR", "" + t.toString());

            }
        });


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListDept);
        sp_dept.setAdapter(customSpinnerAdapter);
        sp_dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //JSONArray deptArray = new JSONArray();
                //JSONObject deptJsonObject = new JSONObject();
                deptValue = parent.getSelectedItem().toString();

                if (deptValue.equalsIgnoreCase("Department")) {
                    sp_roles.setVisibility(View.INVISIBLE);

                } else {

                    Constant.deptName = deptValue;
                    Log.i(" Constant.deptName**", "" + Constant.deptName);

                    getAttendanceDeptBy();

                    // sp_roles.setVisibility(View.VISIBLE);
                   /* try {
                        Constant.ROLES_NAME = deptValue;
                        deptArray.put(Constant.deptName);
                        deptJsonObject.put("departments", deptArray);
                        fileter(deptValue);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getAttendanceDeptBy() {

        mApiService.getAttendanceByDepartment(Constant.SCHOOL_ID, tvTitle2.getText().toString()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Res***qq", "" + response.body() + "***" + response.code() + tvTitle2.getText().toString());
                if (response.isSuccessful()) {
                    empAttendanceModels.clear();
                    Log.i("Res***", "" + response.body() + "***" + response.code());


                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.d("ADMIN_API_DATA", object.toString());

                            if (object.getJSONObject("data").toString().equals("{}")) {
                                Log.d("ADMIN_API_DATA1111111", object.toString());

                            }


                            else {
                                JSONObject jsonObject = object.getJSONObject("data");

                                Iterator<String> key = jsonObject.keys();
                                while (key.hasNext()) {
                                    String myKey = key.next();
                                    Log.d("ADMIN_API_DATA_myKey", myKey);

                                    JSONObject keyData = jsonObject.getJSONObject(myKey);
                                    Log.d("ADMIN_API_KEY_DATA_JSON", keyData.toString());

                                    if (keyData.isNull("added_by_emp_firstname")) {
                                       empFname = "";
                                    } else {
                                        empFname = keyData.getString("added_by_emp_firstname");
                                    }


                                    if (keyData.isNull("added_by_emp_lastname")) {
                                        empLname = "";
                                    } else {
                                        empLname = keyData.getString("added_by_emp_lastname");
                                    }


                                    employee_name = empFname+" "+empLname;

                                    logOut_time = keyData.getString("logOut_time");
                                    empStatus = keyData.getString("status");
                                    total_time = keyData.getString("total_time");
                                    logIn_time = keyData.getString("logIn_time");
                                    attendance_id = keyData.getString("attendance_id");
                                    employee_id = keyData.getString("employee_id");
                                    employee_image = keyData.getString("employee_image");
                                    employee_department = keyData.getString("employee_department");


                                    if (Constant.deptName.equalsIgnoreCase(employee_department)) {
                                        empAttendanceModels.add(new EmpAttendanceModel(employee_name, logOut_time, empStatus,
                                                total_time, logIn_time, attendance_id, employee_id, employee_image, employee_department));
                                    }
                                }

                                if (empAttendanceModels.size()>0) {
                                    setRecyclerView(empAttendanceModels);
                                }else {
                                    empAttendanceModels.clear();
                                    attendanceAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(),"No Records",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                    } catch (JSONException je) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();

        recycler_view = view.findViewById(R.id.recyclerview);
        sp_dept = view.findViewById(R.id.sp_dept);
        sp_roles = view.findViewById(R.id.sp_roles);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        tvTitle.setText("Employee Attendance ");

        employeeList = new ArrayList<>();
        newEmployeeList = new ArrayList<>();
        empAttendanceModels.clear();
        sp_roles.setVisibility(View.GONE);
        // getAllEmployeeAPI();

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = simpleDate.format(calendar.getTime());
        tvTitle2.setText(currentDate);
        tvTitle2.setOnClickListener(this);
        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }


    private void setRecylerView(ArrayList<EmpLeaveModel> employeeList) {
        Gson g = new Gson();

        System.out.println("data****" + g.toJson(employeeList));

        adapter = new EmployeeAdapter(getActivity(), employeeList, Constant.RV_EMPLOYEE_LIST_ATTENDANCE);
        // EmployeeAttendanceAdapter adapter = new EmployeeAttendanceAdapter(getActivity(), this.employeeList, Constant.RV_EMPLOYEE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setRecylerViewFilter(ArrayList<EmpLeaveModel> newEmployeeList) {

        adapter = new EmployeeAdapter(getActivity(), newEmployeeList, Constant.RV_EMPLOYEE_LIST_ATTENDANCE);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                startYear = yearFormat.format(calendar.getTime());
                str_from_date = simpleDateFormat.format(calendar.getTime());

                Log.d("CHECK_DATE", str_from_date);

                tvTitle2.setText(str_from_date);
                Date date2 = new Date();
                try {
                    date2 = simpleDateFormat.parse(str_from_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // getAllEmployeeAPI();
                sp_dept.setSelection(0);

                getEmployeeAttendance();

            }
        }, year, month, date);

          dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }


    private void getEmployeeAttendance() {
        mApiService.getAttendanceByDate(Constant.SCHOOL_ID, tvTitle2.getText().toString()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Res***qq", "" + response.body() + "***" + response.code() + tvTitle2.getText().toString());
                if (response.isSuccessful()) {
                    empAttendanceModels.clear();
                    Log.i("Res***", "" + response.body() + "***" + response.code());


                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");


                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            String Present_Employees = jsonData.getString("Present Employees");
                            String Total_Employees = jsonData.getString("Total Employees");


                            // if (!jsonData.isNull("attendance_data")) {
                            if (jsonData.getJSONObject("attendance_data").toString().equals("{}")) {
                                Log.d("ADMIN_API_EMP", jsonData.toString());
                                empAttendanceModels.clear();
                                employeeList.clear();
                                newEmployeeList.clear();
                                //adapter.notifyDataSetChanged();
                                setRecyclerView(empAttendanceModels);
                                setRecylerView(employeeList);
                                setRecylerViewFilter(newEmployeeList);

                            } else {
                                JSONObject jsonObject = jsonData.getJSONObject("attendance_data");
                                Iterator<String> key = jsonObject.keys();
                                while (key.hasNext()) {
                                    String myKey = key.next();
                                    Log.d("ADMIN_API_DATA_myKey", myKey);

                                    JSONObject keyData = jsonObject.getJSONObject(myKey);
                                    Log.d("ADMIN_API_KEY_DATA_JSON", keyData.toString());


                                    employee_name = keyData.getString("employee_name");
                                    logOut_time = keyData.getString("logOut_time");
                                    empStatus = keyData.getString("status");
                                    total_time = keyData.getString("total_time");
                                    logIn_time = keyData.getString("logIn_time");
                                    attendance_id = keyData.getString("attendance_id");
                                    employee_id = keyData.getString("employee_id");
                                    employee_image = keyData.getString("employee_image");

                                    empAttendanceModels.add(new EmpAttendanceModel(employee_name, logOut_time, empStatus,
                                            total_time, logIn_time, attendance_id, employee_id, employee_image, ""));
                                }

                                setRecyclerView(empAttendanceModels);
                            }
                        }


                    } catch (JSONException je) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView(ArrayList<EmpAttendanceModel> empAttendanceModels) {

         attendanceAdapter = new AttendanceAdapter(getActivity(), empAttendanceModels,
                Constant.RV_ADMIN_ATTENDANCE_LIST_STAFF);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(attendanceAdapter);
        attendanceAdapter.notifyDataSetChanged();
    }




    private void getRoles() {

        JSONObject wingsJsonObject = new JSONObject();
        JSONObject deptJsonObject = new JSONObject();
        JSONArray deptArray = new JSONArray();
        try {
            deptArray.put("All");
            wingsJsonObject.put("wings", deptArray);
            deptJsonObject.put("departments", deptArray);
        } catch (JSONException j) {

        }

        mApiService.gettingRoles(Constant.SCHOOL_ID, wingsJsonObject, deptJsonObject).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        arrayListRoles.clear();
                        arrayListRoles.add("-Roles-");
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("roles");


                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String rolesName = jsonObjectValue.getString("role");
                                boolean roles_status = Boolean.parseBoolean(jsonObjectValue.getString("status"));
                                if (roles_status) {
                                    arrayListRoles.add(rolesName);
                                }
                            }
                            Log.i("makerDATA", "" + arrayListRoles);
                        }

                        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), arrayListRoles);
                        sp_roles.setAdapter(customSpinnerAdapter);
                        sp_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                strRoles = sp_roles.getItemAtPosition(position).toString();
                                System.out.println("strROLS**" + strRoles);

                                //fileter(deptValue, strRoles);
                                fileter(strRoles);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("ROLES_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {

                    }
                    if (String.valueOf(response.code()).equals("404")) {

                    }
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private void getAllEmployeeAPI() {
        mApiService.getAllEmployeeList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    try {
                        employeeNameList.clear();
                        employeeList.clear();
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
                                if (keyData.isNull("custom_employee_id")) {
                                    custom_EMPID = "";
                                } else {
                                    custom_EMPID = keyData.getString("custom_employee_id");
                                }

                                if (keyData.isNull("first_name")) {
                                    empFname = "";
                                } else {
                                    empFname = keyData.getString("first_name");
                                }


                                if (keyData.isNull("last_name")) {
                                    empLname = "";
                                } else {
                                    empLname = keyData.getString("last_name");
                                }

                                if (keyData.isNull("employee_uuid")) {
                                    empUUId = "";
                                } else {
                                    empUUId = keyData.getString("employee_uuid");
                                }


                                if (keyData.isNull("phone_number")) {
                                    empPhoneNo = "";
                                } else {
                                    empPhoneNo = keyData.getString("phone_number");
                                }

                                if (keyData.isNull("adhaar_card_no")) {
                                    empAdhaarNo = "";
                                } else {
                                    empAdhaarNo = keyData.getString("adhaar_card_no");
                                }
                                if (keyData.isNull("department_name")) {
                                    empDept = "";
                                } else {
                                    empDept = keyData.getString("department_name");
                                }
                                if (keyData.isNull("employee_status")) {
                                    employee_status = "";
                                } else {
                                    employee_status = keyData.getString("employee_status");
                                }
                                if (keyData.isNull("wing_name")) {
                                    wing_name = "";
                                } else {
                                    wing_name = keyData.getString("wing_name");
                                }


                                if (keyData.isNull("employee_photo")) {
                                    employee_photo = "";
                                } else {
                                    employee_photo = keyData.getString("employee_photo");
                                }


                                if (keyData.isNull("role")) {
                                    role = "";
                                } else {
                                    role = keyData.getString("role");
                                }

                                if (keyData.isNull("sex")) {
                                    sex = "";
                                } else {
                                    sex = keyData.getString("sex");
                                }


                                String empName = empFname + " " + empLname;
                                employeeNameList.add(empName);
                                if (employee_status.equalsIgnoreCase("Approved")) {
                                    employeeList.add(new EmpLeaveModel(custom_EMPID, empFname, empLname, empUUId, empPhoneNo,
                                            empAdhaarNo, empDept, employee_status, wing_name, employee_photo, role, sex));

                                }

                                System.out.println("data1****" + employeeNameList.size());


                            }
                            setRecylerView(employeeList);

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