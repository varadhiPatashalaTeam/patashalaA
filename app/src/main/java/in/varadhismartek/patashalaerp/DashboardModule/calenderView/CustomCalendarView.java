package in.varadhismartek.patashalaerp.DashboardModule.calenderView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import in.varadhismartek.patashalaerp.ScheduleModule.MyScheduleModel;
import in.varadhismartek.patashalaerp.ScheduleModule.ScheduleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomCalendarView extends LinearLayout {

    private static final String TAG = CustomCalendarView.class.getSimpleName();
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
    private LinearLayout linearLayout;
    private Spinner spn_session, spn_serialNo;
    private APIService mApiServicePatashala;
    private APIService mApiService;
    private ArrayList<String> academicList;
    private ArrayList<String> serialList;
    private DateConvertor convertor;
    private ArrayList<ScheduleModel> eventList;

    private GridAdapter mAdapter;

    private ArrayList<MyScheduleModel> myScheduleList;

    String image, added_by_employee_firstname, holiday_type, from_date,
            added_by_employee_lastname, division, to_date, eventtiltle,strMessage;


    public interface EventListPass {

        void listPass(ArrayList<MyScheduleModel> arrayList);
    }

    private EventListPass eventListPass;

    public void onListPass(EventListPass eventListPass) {
        this.eventListPass = eventListPass;
    }


    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        //setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_calendar_layout, this);

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();

        linearLayout = view.findViewById(R.id.linearLayout);
        spn_session = view.findViewById(R.id.spn_session);
        spn_serialNo = view.findViewById(R.id.spn_serialNo);
        previousButton = view.findViewById(R.id.previous_month);
        nextButton = view.findViewById(R.id.next_month);
        currentDate = view.findViewById(R.id.display_current_date);
        addEventButton = view.findViewById(R.id.add_calendar_event);
        calendarGridView = view.findViewById(R.id.calendar_grid);

        linearLayout.setVisibility(VISIBLE);
        myScheduleList = new ArrayList<>();

        eventList = new ArrayList<>();
        academicList = new ArrayList<>();
        serialList = new ArrayList<>();
        convertor = new DateConvertor();

        getAcademicYearAPI();
    }

    private void setSpinnerForAcademicYear(ArrayList<String> academicList) {

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context, academicList, "Green");
        spn_session.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spn_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String fromDate = "";
                String toDate = "";

                if (i == 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(0, "-Session-");
                    setSpinnerForSerialNo(list, "", "");

                } else {

                    String[] date = spn_session.getSelectedItem().toString().split("/");

                    fromDate = date[0];
                    toDate = date[1];

                    Log.d("asflkaf", "kgdkdsgkgd");
                    getSessionAPI(fromDate, toDate);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerForSerialNo(ArrayList<String> list, final String fromDate, final String toDate) {

        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(context, list, "Green");
        spn_serialNo.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        spn_serialNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (eventList.size() > 0)
                    eventList.clear();

                if (myScheduleList.size() > 0)
                    myScheduleList.clear();

                String serialNo = "";

                if (i == 0) {


                } else {

                    serialNo = spn_serialNo.getSelectedItem().toString();
                    Log.d("slhdlhsf", serialNo);

                   String from = convertor.getDateDMY_to_YMD(fromDate);
                   // String to = convertor.getDateDMY_to_YMD(toDate);

                    /*getExamStatusAPI(serialNo, from, to);
                    getStatusEventsAPI(serialNo, from, to);
                    getStatusHolidaysAPI(serialNo,from,to);*/

                    setScheduletoRecyclerView(serialNo, fromDate, toDate);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setScheduletoRecyclerView(final String serialNo, final String academicStart, final String academicEnd) {

        Log.d("slhdlhsf3", serialNo + " " + academicStart + " " + academicEnd + "" + Constant.SCHOOL_ID);

        mApiService.getScheduleBarriers(Constant.SCHOOL_ID,
                serialNo, academicStart, academicEnd)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                        Log.d("my_responce", response.code()+" lhhlg");

                        if (response.isSuccessful()){

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                //String message = object.getString("message");
                                String status1 = object.getString("status");

                                if (status1.equalsIgnoreCase("Success")){

                                    JSONObject jsonData = object.getJSONObject("data");

                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()){

                                        JSONObject keyData = jsonData.getJSONObject(key.next());

                                        String added_employeeid = "", note = "", added_by_employee_lastname = "", to_date = "",
                                                added_by_employee_firstname = "",added_datetime = "",schedule_title = "",schedule_type = "",
                                                division = "",from_date = "", schedule_image = "";


                                        added_employeeid = keyData.getString("added_employeeid");
                                        note = keyData.getString("note");
                                        added_by_employee_lastname = keyData.getString("added_by_employee_lastname");
                                        to_date = keyData.getString("to_date");
                                        added_by_employee_firstname = keyData.getString("added_by_employee_firstname");
                                        added_datetime = keyData.getString("added_datetime");
                                        schedule_title = keyData.getString("schedule_title");
                                        schedule_type = keyData.getString("schedule_type");
                                        division = keyData.getString("division");
                                        from_date = keyData.getString("from_date");
                                        schedule_image = keyData.getString("schedule_image");

                                        Log.d("ksgkgfd",note);

                                        String date = from_date.substring(0,10);

                                        eventList.add(new ScheduleModel(date, "Event", division));
                                        Log.d("myapi_event", from_date);

                                        if (myScheduleList.size()>0){

                                            boolean b = true;

                                            for (int i = 0; i < myScheduleList.size(); i++){

                                                if (myScheduleList.get(i).getDate().equals(date)){
                                                    b = false;

                                                    myScheduleList.get(i).getScheduleModelList()
                                                            .add(new ScheduleModel(added_employeeid, note, added_by_employee_lastname , to_date,
                                                                    added_by_employee_firstname,added_datetime,schedule_title ,schedule_type,
                                                                    division, from_date , schedule_image, academicStart, academicEnd,serialNo,""));
                                                }
                                            }

                                            if (b){
                                                ArrayList<ScheduleModel> myList = new ArrayList<>();
                                                myList.add(new ScheduleModel(added_employeeid, note, added_by_employee_lastname , to_date,
                                                        added_by_employee_firstname,added_datetime,schedule_title ,schedule_type,
                                                        division, from_date , schedule_image,academicStart, academicEnd,serialNo,""));

                                                myScheduleList.add(new MyScheduleModel(date,myList));
                                            }
                                        }else {
                                            ArrayList<ScheduleModel> list = new ArrayList<>();
                                            list.add(new ScheduleModel(added_employeeid, note, added_by_employee_lastname , to_date,
                                                    added_by_employee_firstname,added_datetime,schedule_title ,schedule_type,
                                                    division, from_date , schedule_image, academicStart, academicEnd,serialNo,""));

                                            myScheduleList.add(new MyScheduleModel(date,list));

                                        }

                                    }

                                   // progressDialog.dismiss();
                                    eventListPass.listPass(myScheduleList);
                                    mAdapter.notifyDataSetChanged();

                                }else {
                                   // progressDialog.dismiss();
                                    //adapter.notifyDataSetChanged();

                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }else {
                            try {
                                assert response.errorBody()!=null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API2", message);
                                //progressDialog.dismiss();
                               // adapter.notifyDataSetChanged();

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

                    }
                });
    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setUpCalendarAdapter() {

        List<Date> dayValueInCells = new ArrayList<>();
        Calendar mCal = (Calendar) cal.clone();

        mCal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;

        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);


        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        Constant.TOOLBAR_MONTH = formatter_month.format(cal.getTime());
        mAdapter = new GridAdapter(context, dayValueInCells, cal, eventList);
        calendarGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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
                            Log.d("jwhdlff", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                String to = convertor.getDateByTZ(end_date);
                               /* String from = convertor.getDateByTZ(start_date);
                                String to = convertor.getDateByTZ(end_date);


*/
                                Log.d("lakfblkas", start_date + " " + end_date);

                                academicList.add(start_date + "/" + end_date);
                            }

                            setSpinnerForAcademicYear(academicList);

                        } else {
                            Log.d("ldafhhlka", response.code() + " " + message);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void getExamStatusAPI(String serialNo, String fromDate, String toDate) {

        Log.d("slhdlhsf1", serialNo + " " + fromDate + " " + toDate + "" + Constant.SCHOOL_ID);

        mApiServicePatashala.getStatusExam(Constant.SCHOOL_ID, serialNo, fromDate, toDate)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {

                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String message1 = object.getString("message");
                                String status1 = object.getString("status");

                                if (status1.equalsIgnoreCase("Success")) {

                                    JSONObject jsonData = object.getJSONObject("data");

                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()) {

                                        JSONObject keyData = jsonData.getJSONObject(key.next());

                                        String added_by_employee_firstname = keyData.getString("added_by_employee_firstname");
                                        String message = keyData.getString("message");
                                        String subject = keyData.getString("subject");
                                        String start_time = keyData.getString("start_time");
                                        String added_by_employee_lastname = keyData.getString("added_by_employee_lastname");
                                        String class1 = keyData.getString("class");
                                        String division = keyData.getString("division");
                                        //String updated_datetime = keyData.getString("updated_datetime");
                                        String exam_type = keyData.getString("exam_type");
                                        String section = keyData.getString("section");
                                        String added_employeeid = keyData.getString("added_employeeid");
                                        String added_datetime = keyData.getString("added_datetime");
                                        String exam_date = keyData.getString("exam_date");

                                        Log.d("ksgkgfd", added_by_employee_firstname);

                                        eventList.add(new ScheduleModel(exam_date, "Exam", division));


                                        if (myScheduleList.size() > 0) {

                                            boolean b = true;

                                            for (int i = 0; i < myScheduleList.size(); i++) {

                                                if (myScheduleList.get(i).getDate().equals(exam_date)) {
                                                    b = false;

                                                    myScheduleList.get(i).getScheduleModelList()
                                                            .add(new ScheduleModel("Exam", exam_type, exam_date, "", start_time, class1,
                                                                    section, division, message, "", added_by_employee_firstname, "", subject));
                                                }
                                            }

                                            if (b) {
                                                ArrayList<ScheduleModel> myList = new ArrayList<>();
                                                myList.add(new ScheduleModel("Exam", exam_type, exam_date, "", start_time, class1,
                                                        section, division, message, "", added_by_employee_firstname, "", subject));

                                                myScheduleList.add(new MyScheduleModel(exam_date, myList));
                                            }
                                        } else {
                                            ArrayList<ScheduleModel> list = new ArrayList<>();
                                            list.add(new ScheduleModel("Exam", exam_type, exam_date, "", start_time, class1,
                                                    section, division, message, "", added_by_employee_firstname, "", subject));

                                            myScheduleList.add(new MyScheduleModel(exam_date, list));

                                        }

                                        Log.d("myapi_exam", exam_date);

                                    }

                                    eventListPass.listPass(myScheduleList);

                                    mAdapter.notifyDataSetChanged();

                                } else {
                                    Log.d("ldafhhlkafhd", response.code() + " " + message1);
                                    Toast.makeText(context, message1, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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



}




























