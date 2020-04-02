package in.varadhismartek.patashalaerp.Timetable;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableMain_Activity extends AppCompatActivity {
    APIService apiService, apiServicePatashala;
    ArrayList<PeriodTableModel> periodTableModels = new ArrayList<>();
    RecyclerView recyclerView_periods, recyclerView_days, recyclerView_data;
    ArrayList<String> dayArrayList = new ArrayList<>();
    ArrayList<Integer> countValue = new ArrayList<>();
    ArrayList<TextView> textViewArrayList = new ArrayList<>();
    LinearLayout lLayout;
    String duration, start_time, addedUUID, end_time, type_of;
    ArrayList<PeriodTableModel> classSectionTimeTable = new ArrayList<>();
    ArrayList<TimetableDetails> timetableDetails;

    String Day, subject, teacher, strStart_time, strEnd_time, strDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout);
        apiService = ApiUtils.getAPIService();
        apiServicePatashala = ApiUtilsPatashala.getService();

        recyclerView_periods = findViewById(R.id.recyclerView);
        recyclerView_days = findViewById(R.id.recyclerView_day);
        recyclerView_data = findViewById(R.id.recyclerView_data);
        recyclerView_data.setVisibility(View.GONE);
        recyclerView_days.setVisibility(View.VISIBLE);
        dayArrayList.clear();


        dayArrayList.add("MON");
        dayArrayList.add("TUS");
        dayArrayList.add("WED");
        dayArrayList.add("THU");
        dayArrayList.add("FRI");
        dayArrayList.add("SAT");
        dayArrayList.add("SUN");

        // TimeTableMain_Activity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //set orientation

        getPeriodMethod();
        getTimeTableClassSection();


    }

    private void getTimeTableClassSection() {
        classSectionTimeTable.clear();

        apiServicePatashala.getClassSectionTimetable(Constant.SCHOOL_ID, "Class 1", "A",
                "1", "2020-06-08")
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("TimeTable**", "" + response.code() + response.body() + response.raw());
                        try {
                            JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                            String status = (String) json1.get("status");
                            if (status.equalsIgnoreCase("Success")) {


                                JSONArray jsonArray = json1.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    Day = jsonObject.getString("Day");
                                    subject = jsonObject.getString("subject");
                                    teacher = jsonObject.getString("teacher");
                                    strStart_time = jsonObject.getString("start_time");
                                    strEnd_time = jsonObject.getString("end_time");
                                    strDuration = jsonObject.getString("duration");


                                    if (Day.contains("Monday")) {
                                        timetableDetails = new ArrayList<>();
                                        timetableDetails.clear();

                                        timetableDetails.add(new TimetableDetails(subject, teacher, strStart_time,
                                                strEnd_time, strDuration));
                                        classSectionTimeTable.add(new PeriodTableModel(Day, timetableDetails));
                                    }else{

                                    }
                                }

                            }
                            Gson gson = new Gson();
                            System.out.println("classSectionTimeTable**" + gson.toJson(classSectionTimeTable));

                        } catch (JSONException je) {

                        }
                        setRecyclerViewDay();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }




    private void getPeriodMethod() {
        apiService.getPeriods(Constant.SCHOOL_ID, "Primary", "1", "2020-06-08")
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        System.out.println("ResponseTable***" + response.body() + "***" + response.code());
                        if (response.isSuccessful()) {
                            periodTableModels.clear();
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                if (status.equalsIgnoreCase("Success")) {

                                    JSONObject jsonObject1 = json1.getJSONObject("data");
                                    Iterator<String> keys = jsonObject1.keys();

                                    while (keys.hasNext()) {
                                        String key = keys.next();

                                        JSONObject dataJson = jsonObject1.getJSONObject(key);


                                        duration = dataJson.getString("duration");
                                        start_time = dataJson.getString("start_time");
                                        addedUUID = dataJson.getString("added_employee_uuid");
                                        end_time = dataJson.getString("end_time");
                                        type_of = dataJson.getString("type_of");
                                        periodTableModels.add(new PeriodTableModel(duration, start_time, addedUUID, end_time, type_of));

                                    }
                                    if (periodTableModels.size() > 0) {
                                        createTextView();
                                        setRecyclerViewDay();
                                    }
                                    setRecyclerView(periodTableModels);
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

    private void createTextView() {
        Log.i("periodTableModels**1", "" + periodTableModels.size());
        Log.i("periodTableModels**2", "" + dayArrayList.size());
        int intDataTotal = periodTableModels.size() * dayArrayList.size();
        int count = 0;
        for (int i = 0; i < intDataTotal; i++) {
            count++;
            countValue.add(count);

        }

        Log.i("periodTableModels**3", "" + intDataTotal + count);


        setRecyclerViewTextView(countValue);


    }

    private void setRecyclerViewTextView(ArrayList<Integer> countValue) {
        recyclerView_data.setHasFixedSize(true);
        TimeTableAdapter periodsAdapter = new TimeTableAdapter(Constant.RV_TIMEYABLE_TEXTVIEW,
                countValue, TimeTableMain_Activity.this);

        recyclerView_data.setLayoutManager(new GridLayoutManager(TimeTableMain_Activity.this, periodTableModels.size()));
        recyclerView_data.setAdapter(periodsAdapter);
    }


    private void setRecyclerViewDay() {
        Log.i("dayName**Day",""+Day);
        recyclerView_days.setHasFixedSize(true);
        //TimeTableAdapter periodsAdapter = new TimeTableAdapter(dayArrayList, TimeTableMain_Activity.this, Constant.RV_TIMEYABLE_DAYS);
        TimeTableAdapter periodsAdapter = new TimeTableAdapter(classSectionTimeTable, Day,
                TimeTableMain_Activity.this, Constant.RV_TIMEYABLE_DAYS);


        recyclerView_days.setLayoutManager(new LinearLayoutManager(TimeTableMain_Activity.this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView_days.setAdapter(periodsAdapter);
    }

    private void setRecyclerView(ArrayList<PeriodTableModel> periodTableModels) {
        recyclerView_periods.setHasFixedSize(true);
        TimeTableAdapter periodsAdapter = new TimeTableAdapter(TimeTableMain_Activity.this,
                periodTableModels, Constant.RV_TIMEYABLE_PERIODS);


        recyclerView_periods.setLayoutManager(new LinearLayoutManager(TimeTableMain_Activity.this,
                LinearLayoutManager.HORIZONTAL, false));

        recyclerView_periods.setAdapter(periodsAdapter);


//        recyclerView_periods.setHasFixedSize(true);


        /* recyclerView = (RecyclerView) findViewById(R.id.recycler);

        imageModelArrayList = eatFruits();
        adapter = new FruitAdapter(this, imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
 */


        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
/*
           TimeTableAdapter  periodsAdapter = new TimeTableAdapter(this, dayDataList, Constant.TAG_DAYS);
            rv_days.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rv_days.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
            rv_days.setItemAnimator(new DefaultItemAnimator());
            rv_days.setAdapter(daysAdapter);*/

    }
}
