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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;

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
import in.varadhismartek.patashalaerp.DashboardModule.Attendance.AttendanceAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.Attendance.EmpAttendanceModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_EmployeeAttendance extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    APIService apiService;
    TextView tvCurrentDate,tv_record;
    AttendanceAdapter attendanceAdapter;
    ArrayList<EmpAttendanceModel> empAttendanceModels = new ArrayList<>();
    String employee_name, logOut_time, empStatus, total_time, logIn_time, attendance_id, employee_id, employee_image,currentDate;

    private int year, month, date, rowNumber = 1;
    private String str_sessionName = "", strSelectSession = "", str_from_date, str_toDate,
            startYear = "", endYear = "", sDate = "", eDate = "";
    public Fragment_EmployeeAttendance() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_attendance, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListener();

        getEmployeeAttendance();
        return view;

    }

    private void getEmployeeAttendance() {
        apiService.getAttendanceByDate(Constant.SCHOOL_ID, tvCurrentDate.getText().toString()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Res***qq", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {
                    empAttendanceModels.clear();
                    tv_record.setVisibility(View.GONE);
                    Log.i("Res***", "" + response.body() + "***" + response.code());


                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");


                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            String Present_Employees = jsonData.getString("Present Employees");
                            String Total_Employees = jsonData.getString("Total Employees");


                            if (jsonData.isNull("attendance_data")) {
                                tv_record.setVisibility(View.VISIBLE);
                                Log.d("ADMIN_API_DATA1111111", jsonData.toString());

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
                                            total_time, logIn_time, attendance_id, employee_id, employee_image,""));
                                }

                                setRecyclerView(empAttendanceModels);
                            }
                        }



                    } catch (JSONException je) {

                    }
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView(ArrayList<EmpAttendanceModel> empAttendanceModels) {

        attendanceAdapter = new AttendanceAdapter(getActivity(), empAttendanceModels, Constant.RV_ADMIN_ATTENDANCE_LIST);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(attendanceAdapter);
        attendanceAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        recyclerView = view.findViewById(R.id.recycler_view);
        tvCurrentDate = view.findViewById(R.id.tv_current);
        tv_record = view.findViewById(R.id.tv_record);


        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDate.format(calendar.getTime());
        tvCurrentDate.setText(currentDate);

    }

    private void initListener() {
        tvCurrentDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_current:
                fromDatePickerDialog();
                break;
        }

    }

    private void fromDatePickerDialog() {
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

                tvCurrentDate.setText(str_from_date);
                Date date2 = new Date();
                try {
                    date2 = simpleDateFormat.parse(str_from_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                getEmployeeAttendance();

            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() );
        dialog.show();
    }
}
