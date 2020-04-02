package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.calenderView.CustomCalendarView;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceCalander extends LinearLayout {

  //  private static final String TAG = in.varadhismartek.patashalaerp.DashboardModule.calenderView.CustomCalendarView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private SimpleDateFormat formatter_month = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    ArrayList<AttendanceModel> list;

    APIService mApiService;

    private AttendenceGridAdapter mAdapter;

    private MyListenres myListenres;

    public interface MyListenres{

        void onRefresh(Calendar calendar);
    }

    public void onRefreshListeners(MyListenres myListenres){

        this.myListenres = myListenres;
    }

    public AttendenceCalander(Context context) {
        super(context);
    }

    public AttendenceCalander(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
      //  Log.d(TAG, "I need to call this method");

    }

    public AttendenceCalander(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_calendar_layout, this);

        mApiService = ApiUtils.getAPIService();
        list = new ArrayList<>();

        previousButton   = view.findViewById(R.id.previous_month);
        nextButton       = view.findViewById(R.id.next_month);
        currentDate      = view.findViewById(R.id.display_current_date);
        addEventButton   = view.findViewById(R.id.add_calendar_event);
        calendarGridView = view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();

                myListenres.onRefresh(cal);
            }
        });
    }

    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();

                myListenres.onRefresh(cal);

            }
        });
    }

    private void setUpCalendarAdapter() {

        List<Date> dayValueInCells = new ArrayList<>();
        Calendar mCal = (Calendar) cal.clone();

        mCal.set(Calendar.DAY_OF_MONTH, 1);
        Log.d("Date Is---------", String.valueOf(mCal.getTime()));

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d("Date Is---------", String.valueOf(firstDayOfTheMonth));

        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        Log.d("Date Is---------", String.valueOf(mCal.getTime()));


        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            Log.d("ARRAY_LIST_CURRENT", String.valueOf(dayValueInCells.size()+" "+mCal.getTime()));
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
            Log.d("ARRAY_LIST_INCREMENT", String.valueOf(mCal.getTime()));

        }

       // Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);


        if(Constant.audience_type.equalsIgnoreCase("STUDENT")){
            getStudentMonthlyAttendanceAPI(cal);
        }else {
            getMonthlyEmployeeAttendanceAPI(cal);


        }
       /* switch (Constant.audience_type){

            case "Staff":
                getMonthlyEmployeeAttendanceAPI(cal);

                break;

            case "Parent":
               // getStudentMonthlyAttendanceAPI(cal);
                break;
        }*/
      //  getMonthlyEmployeeAttendanceAPI(cal);

        Log.d("size_list", String.valueOf(list.size()));

        mAdapter = new AttendenceGridAdapter(context, dayValueInCells, cal,list,myDateClickListener);
        calendarGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Constant.CLONE_CALANDAR = cal;

    }

    private void getMonthlyEmployeeAttendanceAPI(Calendar cal){

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        Log.d("MY_YEAR", year+" "+(month+1));
        Log.d("EMPLOYEE_ID_ATT", Constant.EMPLOYEE_ID);

        list.clear();

        mApiService.getEmpAttendance(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
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


                                     Log.d("logIn_time", ""+logIn_time+logOut_time);
                                     list.add(new AttendanceModel(myKey+"-"+(month+1)+"-"+year, attend,total_time,added_datetime,
                                             logOut_time,logIn_time,added_by,attendance_id));

                                 }


                                 Log.d("aksdjkla_1", ""+list.size());
                                 mAdapter.notifyDataSetChanged();

                            }

                        }else {

                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
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





    private void getStudentMonthlyAttendanceAPI(Calendar cal){

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        Log.d("MY_YEAR_Student", year+" "+(month+1)+"**"+Constant.EMPLOYEE_ID);

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




                                    list.add(new AttendanceModel(myKey+"-"+(month+1)+"-"+year,
                                            status,"",added_datetime,
                                            "","",added_by,student_attendance_id));

                                }


                                Log.d("aksdjkla", ""+list.size());
                                mAdapter.notifyDataSetChanged();

                            }

                        }else {

                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
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



    MyDateClickListener myDateClickListener = new MyDateClickListener() {
        @Override
        public void dateClick(String loginTime, String logoutTime, String totalTime) {

            myDateListenrs.dateListener(loginTime, logoutTime,totalTime);
        }
    };

    public interface MyDateClickListener{

        void dateClick(String loginTime, String logoutTime, String totalTime);
    }




    private MyDateListenrs myDateListenrs;

    public interface MyDateListenrs{

        void dateListener(String loginTime, String logoutTime, String totalTime);
    }

    public void onRefreshDateListeners(MyDateListenrs myDateListenres){

        this.myDateListenrs = myDateListenres;
    }

}
