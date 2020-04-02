package in.varadhismartek.patashalaerp.ScheduleModule;


import android.app.ProgressDialog;
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
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.MyModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleListFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    FloatingActionButton fab_button;
    RecyclerView rcv_exam, rcv_event, rcv_holiday;
    ScheduleModel scheduleModel;
    String academicStart = "", academicLast = "", sessionNo = "";
    APIService mApiServicePatashala,mApiService;
    ProgressDialog progressDialog;

    ArrayList<MyScheduleModel> myScheduleModelsExam, myScheduleModelsEvent, myScheduleModelsHoliday;
    ScheduleAdapter examAdapter, eventAdapter, holidayAdapter;

    public ScheduleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);

        initViews(view);
        initListeners();
        getBundles();

        return view;
    }

    private void setRecyclerViewForExam() {

        examAdapter = new ScheduleAdapter(myScheduleModelsExam, getActivity(), Constant.RV_SCHEDULE_LIST_ROW);
        rcv_exam.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv_exam.setAdapter(examAdapter);
    }

    private void setRecyclerViewForEvent() {

        eventAdapter = new ScheduleAdapter(myScheduleModelsEvent, getActivity(), Constant.RV_SCHEDULE_LIST_ROW);
        rcv_event.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv_event.setAdapter(eventAdapter);
    }

    private void setRecyclerViewForHoliday() {

        holidayAdapter = new ScheduleAdapter(myScheduleModelsHoliday, getActivity(), Constant.RV_SCHEDULE_LIST_ROW);
        rcv_holiday.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv_holiday.setAdapter(holidayAdapter);
    }

    private void getBundles() {

        Bundle bundle = getArguments();
        scheduleModel = (ScheduleModel) bundle.getSerializable("ScheduleModel");

        academicStart = scheduleModel.getAcademicStart();
        academicLast = scheduleModel.getAcademicEnd();
        sessionNo = scheduleModel.getSessionNo();

        String scheduleType = scheduleModel.getSchedule_type();
        Log.d("scheduleType", scheduleType);

        switch (scheduleType){

            case "Exam":
                setRecyclerViewForExam();
                getStatusExamsAPI();
                break;

            case "Event":
                setRecyclerViewForEvent();
                getStatusEventsAPI();

                break;

            case "Holiday":
                setRecyclerViewForHoliday();
                getStatusHolidays();

                break;
        }

    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        fab_button.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        rcv_exam = view.findViewById(R.id.rcv_exam);
        rcv_event = view.findViewById(R.id.rcv_event);
        rcv_holiday = view.findViewById(R.id.rcv_holiday);
        fab_button = view.findViewById(R.id.fab_button);

        myScheduleModelsExam = new ArrayList<>();
        myScheduleModelsEvent = new ArrayList<>();
        myScheduleModelsHoliday = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();

                break;

            case R.id.fab_button:
                ScheduleActivity scheduleActivity = (ScheduleActivity) getActivity();

                Bundle bundle = new Bundle();
                bundle.putSerializable("ScheduleModel", scheduleModel);
                scheduleActivity.loadFragment(Constant.ADD_SCHEDULE_MANAGEMENT_FRAGMENT ,bundle);

               break;
        }
    }

    private void getStatusExamsAPI() {

        progressDialog. show();

        mApiServicePatashala.getStatusExam("05861673-1073-4029-b04d-6db1b1372a1d","1",
                "2020-03-12","2021-03-01").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message1 = object.getString("message");

                        if (status1.equalsIgnoreCase("Success")){

                            Log.d("getExams_success", message1+" "+response.code()+" "+response.body());

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i< jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String to_date = jsonObject.getString("to_date");
                                String from_date = jsonObject.getString("from_date");

                                String divisionName ="";
                                String examName = "";

                                JSONArray divisionArray = jsonObject.getJSONArray("divisions");

                                for (int j=0; j<divisionArray.length(); j++){

                                    JSONObject divisionObject = divisionArray.getJSONObject(j);

                                    Iterator<String> key = divisionObject.keys();

                                    while (key.hasNext()){

                                        divisionName = key.next();

                                        JSONArray keyArray = divisionObject.getJSONArray(divisionName);

                                        for (int k = 0; k<keyArray.length(); k++){

                                            JSONObject divisionObj = keyArray.getJSONObject(k);
                                            Iterator<String> examKey = divisionObj.keys();
                                            ArrayList<MyNewModel> examListDetail = new ArrayList<>();

                                            while (examKey.hasNext()){

                                                examName = examKey.next();
                                                JSONObject examTypeObj = divisionObj.getJSONObject(examName);
                                                Iterator<String> dateKey = examTypeObj.keys();

                                                while (dateKey.hasNext()) {

                                                    String dateName = dateKey.next();
                                                    JSONArray dateArray = examTypeObj.getJSONArray(dateName);
                                                    String start_time = "", subject = "", section ="", className = "";

                                                    for (int l = 0; l < dateArray.length(); l++) {

                                                        JSONObject dateObject = dateArray.getJSONObject(l);
                                                        Iterator<String> classKey = dateObject.keys();

                                                        ArrayList<ScheduleModel> scheduleModels = new ArrayList<>();

                                                        while (classKey.hasNext()) {

                                                            className = classKey.next();
                                                            JSONArray classArray = dateObject.getJSONArray(className);

                                                            for (int m = 0; m < classArray.length(); m++) {

                                                                JSONObject classData = classArray.getJSONObject(m);

                                                                start_time = classData.getString("start_time");
                                                                String added_datetime = classData.getString("added_datetime");
                                                                String exam_duration = classData.getString("exam_duration");
                                                                subject = classData.getString("subject");
                                                                String added_employeeid = classData.getString("added_employeeid");
                                                                String added_by_employee_firstname = classData.getString("added_by_employee_firstname");
                                                                String added_by_employee_lastname = classData.getString("added_by_employee_lastname");
                                                                String message = classData.getString("message");
                                                                section = classData.getString("section");

                                                                scheduleModels.add(new ScheduleModel(className,section,
                                                                        start_time,subject,exam_duration, message));
                                                            }


                                                        }

                                                        examListDetail.add(new MyNewModel(dateName+" "+examName, scheduleModels));

                                                    }
                                                    myScheduleModelsExam.add(new MyScheduleModel("Exam",from_date, to_date,
                                                            divisionName, examName, examListDetail));
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            progressDialog.dismiss();
                            examAdapter.notifyDataSetChanged();

                        }else {
                            progressDialog.dismiss();
                            Log.d("getExams_fail", message1+" "+response.code());

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("getExams_fail", message+" "+response.code());

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

    private void getStatusHolidays(){

        progressDialog.show();

        mApiService.getStatusHolidays("05861673-1073-4029-b04d-6db1b1372a1d","1",
                "2020-03-12","2021-03-01").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject jsonData = jsonArray.getJSONObject(i);

                                String from_date = jsonData.getString("from_date");
                                String to_date = jsonData.getString("to_date");

                                String divisionName = "", holidayName = "";

                                ArrayList<MyModel> holidayListDetail = new ArrayList<>();

                                JSONArray divisionArray = jsonData.getJSONArray("divisions");

                                for (int j=0; j<divisionArray.length(); j++){

                                    JSONObject divisionObject = divisionArray.getJSONObject(j);

                                    Iterator<String> divisionKey = divisionObject.keys();

                                    while (divisionKey.hasNext()){

                                        divisionName = divisionKey.next();
                                        JSONArray array = divisionObject.getJSONArray(divisionName);

                                        for (int k=0; k<array.length(); k++){

                                            JSONObject divisionData = array.getJSONObject(k);

                                            Iterator<String> holidayKey = divisionData.keys();

                                            while (holidayKey.hasNext()){

                                                holidayName = holidayKey.next();
                                                JSONObject holidayData = divisionData.getJSONObject(holidayName);

                                                Iterator<String> classKey = holidayData.keys();

                                                ArrayList<MyNewModel> myList = new ArrayList<>();

                                                while (classKey.hasNext()){

                                                    String className = classKey.next();

                                                    Log.d("classData", className+" ");

                                                    JSONObject classData = holidayData.getJSONObject(className);

                                                    String added_by_firstname = classData.getString("added_by_firstname");
                                                    String holiday_image = classData.getString("holiday_image");
                                                    String added_by_uuid = classData.getString("added_by_uuid");
                                                    String added_datetime = classData.getString("added_datetime");
                                                    String message = classData.getString("message");
                                                    String holiday_name = classData.getString("holiday_name");
                                                    String added_by_lastname = classData.getString("added_by_lastname");

                                                    Log.d("classData", added_by_firstname+" "+added_by_lastname);

                                                    myList.add(new MyNewModel(className, message, holiday_name, holiday_image));
                                                }

                                                myScheduleModelsHoliday.add(new MyScheduleModel("Holiday",
                                                        from_date, to_date, divisionName, holidayName,myList ));

                                            }

                                            progressDialog.dismiss();
                                            holidayAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }



                        }else {
                            progressDialog.dismiss();
                            holidayAdapter.notifyDataSetChanged();
                            Log.d("getHolidays_fail", message1+" "+response.code());

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        progressDialog.dismiss();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        holidayAdapter.notifyDataSetChanged();
                        Log.d("getHolidays_fail", message+" "+response.code());

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

    private void getStatusEventsAPI(){

        mApiService.getStatusEvents("05861673-1073-4029-b04d-6db1b1372a1d","1",
                "2020-03-12","2021-03-01").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message1 = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")){
                            Log.d("getEvents_success", message1+" "+response.code()+" "+response.body());

                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i=0; i<jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String from_date = jsonObject.getString("from_date");
                                String to_date = jsonObject.getString("to_date");

                                String divisionName = "", eventName = "";

                                ArrayList<MyModel> eventListDetail = new ArrayList<>();

                                JSONArray divisionArray = jsonObject.getJSONArray("divisions");

                                for (int j=0; j<divisionArray.length(); j++){

                                    JSONObject divisionObject = divisionArray.getJSONObject(j);
                                    Iterator<String> divisionKey = divisionObject.keys();

                                    while (divisionKey.hasNext()){

                                        divisionName = divisionKey.next();

                                        JSONArray array = divisionObject.getJSONArray(divisionName);
                                        for (int k=0; k<array.length(); k++){

                                            JSONObject data = array.getJSONObject(k);

                                            Iterator<String> eventKey = data.keys();
                                            while (eventKey.hasNext()){

                                                eventName = eventKey.next();

                                                JSONObject eventData = data.getJSONObject(eventName);

                                                Log.d("eventName", eventName);

                                                Iterator<String> classKey = eventData.keys();

                                                ArrayList<MyNewModel> myList = new ArrayList<>();

                                                while (classKey.hasNext()){
                                                    String className = classKey.next();

                                                    JSONObject classData = eventData.getJSONObject(className);

                                                    String added_by_firstname = classData.getString("added_by_firstname");
                                                    String added_by_lastname = classData.getString("added_by_lastname");
                                                    String added_by_uuid = classData.getString("added_by_uuid");
                                                    String event_title = classData.getString("event_title");
                                                    String message = classData.getString("message");
                                                    String image = classData.getString("image");
                                                    String added_datetime = classData.getString("added_datetime");

                                                    myList.add(new MyNewModel(className, message, event_title, image));

                                                }

                                                myScheduleModelsEvent.add(new MyScheduleModel("Event",
                                                        from_date, to_date, divisionName, eventName, myList ));
                                            }
                                        }
                                    }
                                }
                            }

                            progressDialog.dismiss();
                            eventAdapter.notifyDataSetChanged();

                        }else {
                            Log.d("getEvents_else", message1+" "+response.code());
                            progressDialog.dismiss();
                            eventAdapter.notifyDataSetChanged();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    try {
                        progressDialog.dismiss();
                        eventAdapter.notifyDataSetChanged();
                        assert response.errorBody()!=null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("getEvents_fail", message+" "+response.code());

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
}
