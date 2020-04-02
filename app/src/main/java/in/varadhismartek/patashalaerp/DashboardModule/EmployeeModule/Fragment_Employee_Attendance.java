package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.PieChart.ChartData;
import in.varadhismartek.patashalaerp.PieChart.PieChart;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


    public class Fragment_Employee_Attendance extends Fragment implements View.OnClickListener {

        ImageView iv_backBtn;
        TextView tv_name, tv_id, tv_inTime,tv_outTime, tv_workHours, tv_totalBreak, tv_viewMore;
        AttendenceCalander cal_attendance;

        LinearLayout ll_pieChart;
        LinearLayout ll_others;
        CardView cv_graph;

        ArrayList<AttendanceModel> list;
        APIService mApiService;
        PieChart pieChart;
        String empID,empFirstName,empLanme;

        public Fragment_Employee_Attendance() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_employee_attendance, container, false);

            getBundle();
            initViews(view);
            initListeners();
            setStrings();

            if(Constant.audience_type.equalsIgnoreCase("STUDENT")){
                getMonthlyStudentttendanceAPI(Constant.CLONE_CALANDAR);
            }else {
                getMonthlyEmployeeAttendanceAPI(Constant.CLONE_CALANDAR);

            }


/*            switch (Constant.audience_type){

                case "Staff":
                    cv_graph.setVisibility(View.VISIBLE);
                    ll_others.setVisibility(View.VISIBLE);
                    getMonthlyEmployeeAttendanceAPI(Constant.CLONE_CALANDAR);

                    break;

                case "Parent":
                    cv_graph.setVisibility(View.GONE);
                    ll_others.setVisibility(View.GONE);
                    break;
            }*/


            cal_attendance = view.findViewById(R.id.cal_attendance);

            cal_attendance.onRefreshListeners(new AttendenceCalander.MyListenres() {
                @Override
                public void onRefresh(Calendar calendar) {

                    Log.d("lafk","sdhshdlsdjlsjdksjd");
                    getMonthlyEmployeeAttendanceAPI(calendar);
                }
            });

            cal_attendance.onRefreshDateListeners(new AttendenceCalander.MyDateListenrs() {
                @Override
                public void dateListener(String loginTime, String logoutTime, String totalTime) {
                    Log.d("lafk",""+loginTime);
                    Log.d("lafk",""+logoutTime);
                    Log.d("lafk",""+totalTime);
                    tv_inTime.setText(loginTime);
                    tv_outTime.setText(logoutTime);
                    tv_workHours.setText(totalTime);
                }
            });

            return view;
        }




        private void getBundle() {
            Bundle b = getArguments();
            if (b != null) {
                empID = b.getString("EMPUUID");
                empFirstName = b.getString("FNAME");
                empLanme = b.getString("LNAME");
                Log.i("empID**1",""+empID);
            }
            else {
                empID =b.getString("EMPUUID");
                Log.i("empID**2",""+empID);

            }

            Constant.EMPLOYEE_ID =empID;

            Log.i("empFirstName**",""+empFirstName+"**"+empLanme+empID);

        }

        private void setStrings() {

            tv_name.setText(empFirstName+" "+empLanme);
            tv_id.setText("");
            tv_totalBreak.setText("01:30 PM");
            tv_id.setVisibility(View.GONE);
            tv_id.setVisibility(View.GONE);
        }

        private void setChartAndData(int onTime, int absent, int late) {

            int total = onTime + absent + late;
            List<ChartData> data = new ArrayList<>();

            int onTimePercent = 0;
            int absentPercent = 0;
            int latePercent   = 0;

            if (onTime == 0 && absent == 0 && late == 0) {
                absentPercent = 100;
                data.add(new ChartData("", absentPercent, Color.WHITE, Color.parseColor("#CAC6C6")));//red

            }

            if (late != 0) {
                latePercent = (late * 100) / total;
                data.add(new ChartData(latePercent+"%",latePercent , Color.WHITE, Color.parseColor("#f7941d")));//gold

            }

            if (absent != 0) {
                absentPercent = (absent * 100) / total;
                data.add(new ChartData(absentPercent+"%", absentPercent, Color.WHITE, Color.parseColor("#FD0202")));//red

            }

            if (onTime != 0) {
                onTimePercent = (onTime * 100) / total;
                data.add(new ChartData(onTimePercent+"%", onTimePercent, Color.WHITE, Color.parseColor("#72d548")));//green

            }





            Log.d("Percent",onTimePercent+" "+absentPercent+" "+latePercent);

            pieChart.setAboutChart( onTime+late +" / "+total);


            pieChart.setChartData(data);
            pieChart.partitionWithPercent(true);
            pieChart.setAboutTextSize(30);
            pieChart.setCenterCircleColor(Color.WHITE);

        }

        private void initListeners(){

            iv_backBtn.setOnClickListener(this);
            tv_viewMore.setOnClickListener(this);
        }

        private void initViews(View view) {

            ll_pieChart = view.findViewById(R.id.ll_pieChart);

            pieChart= new PieChart(getActivity());
            pieChart.setTextColor(R.color.green);
            pieChart.setChartColor(R.color.green);
            pieChart.setAboutTextColor(R.color.green);
            ll_pieChart.addView(pieChart);

            iv_backBtn = view.findViewById(R.id.iv_backBtn);

            tv_name = view.findViewById(R.id.tv_name);
            tv_id = view.findViewById(R.id.tv_id);
            tv_inTime = view.findViewById(R.id.tv_inTime);
            tv_outTime = view.findViewById(R.id.tv_outTime);
            tv_workHours = view.findViewById(R.id.tv_workHours);
            tv_totalBreak = view.findViewById(R.id.tv_totalBreak);
            tv_viewMore = view.findViewById(R.id.tv_viewMore);

            ll_others = view.findViewById(R.id.ll_others);
            cv_graph = view.findViewById(R.id.cv_graph);

            mApiService = ApiUtils.getAPIService();
            list = new ArrayList<>();

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.iv_backBtn:
                    getActivity().onBackPressed();
                    break;

                case R.id.tv_viewMore:
                    Toast.makeText(getActivity(), "View More Clicked", Toast.LENGTH_SHORT).show();
                    break;

            }

        }

        private void getMonthlyEmployeeAttendanceAPI(Calendar cal){

            final int year = cal.get(Calendar.YEAR);
            final int month = cal.get(Calendar.MONTH);
            Log.d("MY_YEAR", year+" "+(month+1));

            Log.d("EMPLOYEE_ID_FRA_EMP_ATT", Constant.EMPLOYEE_ID);

            if(list.size()>0)
                list.clear();

            mApiService.getEmpAttendance(Constant.SCHOOL_ID, empID).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if (response.isSuccessful()){

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                            String status = object.getString("status");

                            if (status.equalsIgnoreCase("Success")){

                                JSONObject jsonData = object.getJSONObject("data");

                                Log.d("JSON_DATA", jsonData.toString());

                                String employee_id = jsonData.getString("employee_id");
                                JSONObject studentData = jsonData.getJSONObject("student_data");
                                Log.d("JSON_DATA_STU", studentData.toString());

                                JSONObject yearData = studentData.getJSONObject(String.valueOf(year));
                                Log.d("JSON_DATA_STU_year", yearData.toString());

                                if (yearData.getJSONObject(String.valueOf(month+1)).toString().equals("{}")){

                                    Log.d("JSON_DATA_STU_month", (month+1)+" not Exist");

                                }else {

                                    Log.d("JSON_DATA_STU_month", (month+1)+" Exist");

                                    JSONObject monthData = yearData.getJSONObject(String.valueOf(month+1));
                                    Log.d("JSON_DATA_STU_month", monthData.toString());

                                    Iterator<String> key = monthData.keys();

                                    while (key.hasNext()){

                                        String myKey = key.next();
                                        Log.d("MY_KEY", myKey);

                                        JSONObject keyData = monthData.getJSONObject(myKey);
                                        Log.d("MY_KEY_DATA", keyData.toString());

                                        String attend = keyData.getString("status");
                                        String total_time = keyData.getString("total_time");
                                        String added_datetime = keyData.getString("added_datetime");
                                        String logOut_time = keyData.getString("logOut_time");
                                        String logIn_time = keyData.getString("logIn_time");
                                        String added_by = keyData.getString("added_by");
                                        String attendance_id = keyData.getString("attendance_id");


                                        Log.d("logIn_time_mainClass", ""+logIn_time+logOut_time);
                                        list.add(new AttendanceModel(myKey+"-"+(month+1)+"-"+year, attend,total_time,added_datetime,
                                                logOut_time,logIn_time,added_by,attendance_id));

                                    }

                                    Log.d("aksdjkla", ""+list.size());
                                    //refreshChart(list);

                                }

                            }else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        refreshChart(list);


                    }

                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                }
            });

        }

        private void refreshChart(ArrayList<AttendanceModel> myList){

            int onTime  = 0;
            int absent  = 0;
            int late  = 0;

            for (int i = 0; i<myList.size();i++){

                switch (myList.get(i).getStatus()){

                    case "Present":
                        onTime = onTime + 1;
                        break;

                    case "Absent":
                        absent = absent + 1;
                        break;

                    case "Late":
                        late = late + 1;
                        break;
                }

            }

            Log.d("fjslahfl", onTime+" "+absent+" "+late);

            if (myList.size()>0) {
                for (int i=0;i<myList.size();i++){
                    System.out.println("INTIMEOUTTIME**"+myList.get(i).getLogIn_time());
                    System.out.println("INTIMEOUTTIME**2"+myList.get(i).getLogOut_time());
                }
                tv_inTime.setText(myList.get(0).getLogIn_time());
                tv_outTime.setText(myList.get(0).getLogOut_time());
                tv_workHours.setText(myList.get(0).getTotal_time());

            }else {
                tv_inTime.setText("NA");
                tv_outTime.setText("NA");
                tv_workHours.setText("NA");
            }
            setChartAndData(onTime, absent, late);
        }


        private void getMonthlyStudentttendanceAPI(Calendar cal) {

            final int year = cal.get(Calendar.YEAR);
            final int month = cal.get(Calendar.MONTH);
            Log.d("MY_YEAR", year+" "+(month+1));

            list.clear();

            mApiService.getStudentMonthlyAttendance(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                    if (response.isSuccessful()){

                        try {
                            JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                            String status1 = object.getString("status");

                            if (status1.equalsIgnoreCase("Success")){

                                JSONObject jsonData = object.getJSONObject("data");

                                Log.d("JSON_DATA", jsonData.toString());

                                String student_id = jsonData.getString("student_id");
                                JSONObject studentData = jsonData.getJSONObject("student_data");
                                Log.d("JSON_DATA_STU", studentData.toString());

                                JSONObject yearData = studentData.getJSONObject(String.valueOf(year));
                                Log.d("JSON_DATA_STU_year", yearData.toString());

                                if (yearData.getJSONObject(String.valueOf(month+1)).toString().equals("{}")){

                                    Log.d("JSON_DATA_STU_month", (month+1)+" not Exist");

                                }else {

                                    Log.d("JSON_DATA_STU_month", (month+1)+" Exist");

                                    JSONObject monthData = yearData.getJSONObject(String.valueOf(month+1));
                                    Log.d("JSON_DATA_STU_month", monthData.toString());

                                    Iterator<String> key = monthData.keys();

                                    while (key.hasNext()){

                                        String myKey = key.next();
                                        Log.d("MY_KEY", myKey);

                                        JSONObject keyData = monthData.getJSONObject(myKey);
                                        Log.d("MY_KEY_DATA", keyData.toString());

                                        String section = keyData.getString("section");
                                        String session_from = keyData.getString("session_from");
                                        String added_by = keyData.getString("added_by");
                                        String status = keyData.getString("status");
                                        String student_attendance_id = keyData.getString("student_attendance_id");
                                        String classes = keyData.getString("classes");
                                        String added_datetime = keyData.getString("added_datetime");
                                        String session = keyData.getString("session");
                                        String school = keyData.getString("school");
                                        String session_to = keyData.getString("session_to");


                                        list.add(new AttendanceModel(myKey+"-"+(month+1)+"-"+year, status,"",added_datetime,
                                                "","",added_by,student_attendance_id));
                                    }

                                 //   mAdapter.notifyDataSetChanged();

                                }

                            }else {

                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
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
