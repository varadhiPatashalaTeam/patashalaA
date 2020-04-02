package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import in.varadhismartek.patashalaerp.ScheduleModule.ScheduleAdapter;
import in.varadhismartek.patashalaerp.ScheduleModule.ScheduleModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ScheduleBarriersList extends Fragment {
    APIService apiService, mApiServicePatashala;
    private ArrayList<String> serialList;
    private ArrayList<String> academicList, listDivision, listClass, listSection, sessionList, listSectionNew,
            listscheduleType;
    Spinner sp_academic, spn_division, spn_class, spn_section, sp_sessionNo, spn_schedule_type;
    String strAcdaemicYear, acdaemicFromDate, acdaemicToDate, strFromDate, strToDate, strScheduleType;
    private DateConvertor convertor;
    private Utilities utilities;
    String fromDate, toDate, serialNo;
    TextView tvTitle;
    ImageView iv_backBtn;

    String note, added_datetime, to_date, schedule_title,
            schedule_image, added_by_employee_lastname, division,
            added_by_employee_firstname, schedule_type, from_date;
    ArrayList<ScheduleModel> scheduleBarriesList = new ArrayList<>();
    RecyclerView recycler_view;
    FloatingActionButton fab;
    ScheduleAdapter adapter;
    public Fragment_ScheduleBarriersList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_barries_list, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        getAcademicYearAPI();
        return view;
    }

    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        mApiServicePatashala = ApiUtilsPatashala.getService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tvTitle = view.findViewById(R.id.tvTitle);
        sp_academic = view.findViewById(R.id.sp_academic);
        sp_sessionNo = view.findViewById(R.id.sp_sessionNo);
        recycler_view = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        academicList = new ArrayList<>();
        serialList = new ArrayList<>();
        convertor = new DateConvertor();
        tvTitle.setText("Schedule Barriers");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExamActivity examActivity = (ExamActivity) getActivity();
                examActivity.loadFragment(Constant.ADD_SCHEDULE_BARRIERS, null);
            }
        });

        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });
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
                Log.d("AcademicYear****A", ""+response.body());
                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String message = object.getString("message");
                        String status1 = object.getString("status");

                        if (status1.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("AcademicYear", jsonData.toString());

                            JSONObject academicYear = jsonData.getJSONObject("Academic_years");

                            Iterator<String> key = academicYear.keys();

                            while (key.hasNext()) {

                                JSONObject years = academicYear.getJSONObject(key.next());

                                String start_date = years.getString("start_date");
                                String end_date = years.getString("end_date");

                                Log.i("AcademicYear_11", start_date + " " + end_date);

                               // String from = convertor.getDateByTZ(start_date);
                                //String to = convertor.getDateByTZ(end_date);

                                Log.i("AcademicYear_12", start_date + " " + end_date);

                                academicList.add(start_date + "/" + end_date);
                                Log.i("AcademicYear_2", "" + academicList.size());
                            }
                            Log.i("lakfblkasAAAA", "" + academicList.size());


                            setSpinnerForAcademicYear(academicList);

                        } else {

                            Log.d("AcademicYear_3", response.code() + " " + message);
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
        Log.d("asflkaf1", "kgdkdsgkgd");
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), academicList);
        sp_academic.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sp_academic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("asflkaf2", "kgdkdsgkgd" + i);


                if (i == 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(0, "-Session-");
                    setSpinnerForSerialNo(list, "", "");

                } else {

                    strAcdaemicYear = sp_academic.getSelectedItem().toString();
                    String[] date = strAcdaemicYear.split("/");

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

    private void getSessionAPI(final String fromDate, final String toDate) {

        if (serialList.size() > 0) {
            serialList.clear();
        }

        serialList.add(0, "-Session-");

        Log.i("fromDate***",""+fromDate+"**"+toDate);
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
                        Log.d("ADMIN_API2", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    private void setSpinnerForSerialNo(ArrayList<String> list, final String fromDate, final String toDate) {

        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(getActivity(), list, "Green");
        sp_sessionNo.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        sp_sessionNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (i == 0) {


                } else {

                    serialNo = sp_sessionNo.getSelectedItem().toString();
                    Log.d("slhdlhsf", serialNo);

                    String from = convertor.getDateDMY_to_YMD(fromDate);
                    String to = convertor.getDateDMY_to_YMD(toDate);

                    Log.i("fromDate***2",""+fromDate+"**"+toDate);
                    Log.i("fromDate***3",""+from+"**"+to);

                    getScheduleList(from,to);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    private void getScheduleList(String from, String to) {


        final String academic_start_date = from;
        final String academic_end_date = to;
        final String session_serial_no = serialNo;

        if (scheduleBarriesList.size() > 0)
            scheduleBarriesList.clear();

        Log.i("fromDate***4",""+fromDate+"**"+toDate+Constant.SCHOOL_ID+"***"+serialNo);

        utilities.displayProgressDialog(getActivity(), "Processing...", false);

        apiService.getScheduleBarriers(Constant.SCHOOL_ID, serialNo, fromDate, toDate)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                       // Log.d("SCHEDUL_LIST", "" + response.code() + response.body());
                        if (response.isSuccessful()) {
                            utilities.cancelProgressDialog();

                            try {

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                Log.d("SCHEDUL_LIST", "" +object);

                                String message = object.getString("message");
                                String status1 = object.getString("status");

                                if (status1.equalsIgnoreCase("Success")) {

                                    JSONObject jsonData = object.getJSONObject("data");

                                    Iterator<String> key = jsonData.keys();

                                    while (key.hasNext()) {

                                        JSONObject dataValue = jsonData.getJSONObject(key.next());

                                        /*"schedule_type":"Holiday",
         "added_by_employee_lastname":"Kumar",
         "added_employeeid":"e20dd4be-a3c3-4014-b6ce-7e58ed609399",
         "schedule_image":"varadhi_smartech\/school\/Schedule\/car-4028205_960_720.jpg",
         "added_datetime":"2020-03-12",
         "division":"Primary",
         "to_date":"2020-03-13",
         "schedule_title":"New Schedule",
         "note":"corona virus",
         "added_by_employee_firstname":"Abhishek",
         "schedule_id":7,
         "from_date":"2020-03-12"*/


                                        if (dataValue.isNull("note")) {
                                            note = "";
                                        } else {
                                            note = dataValue.getString("note");
                                        }

                                        if (dataValue.isNull("added_datetime")) {
                                            added_datetime = "";
                                        } else {
                                            added_datetime = dataValue.getString("added_datetime");
                                        }
                                        if (dataValue.isNull("to_date")) {
                                            to_date = "";
                                        } else {
                                            to_date = dataValue.getString("to_date");
                                        }

                                        if (dataValue.isNull("added_by_employee_firstname")) {
                                            added_by_employee_firstname = "";
                                        } else {
                                            added_by_employee_firstname = dataValue.getString("added_by_employee_firstname");
                                        }
                                        if (dataValue.isNull("division")) {
                                            division = "";
                                        } else {
                                            division = dataValue.getString("division");
                                        }

                                        if (dataValue.isNull("added_by_employee_lastname")) {
                                            added_by_employee_lastname = "";
                                        } else {
                                            added_by_employee_lastname = dataValue.getString("added_by_employee_lastname");
                                        }

                                        if (dataValue.isNull("schedule_image")) {
                                            schedule_image = "";
                                        } else {
                                            schedule_image = dataValue.getString("schedule_image");
                                        }

                                        if (dataValue.isNull("schedule_title")) {
                                            schedule_title = "";
                                        } else {
                                            schedule_title = dataValue.getString("schedule_title");
                                        }
                                        if (dataValue.isNull("schedule_type")) {
                                            schedule_type = "";
                                        } else {
                                            schedule_type = dataValue.getString("schedule_type");
                                        }
                                        if (dataValue.isNull("from_date")) {
                                            from_date = "";
                                        } else {
                                            from_date = dataValue.getString("from_date");
                                        }


                                        scheduleBarriesList.add(new ScheduleModel(note, added_datetime,
                                                to_date, schedule_title, schedule_image, added_by_employee_lastname,
                                                division, added_by_employee_firstname, schedule_type, from_date,
                                                academic_start_date, academic_end_date, session_serial_no, "type"));
                                    }

                                    Log.i("scheduleBarriesList***",""+scheduleBarriesList.size());
                                    setRecyclerView(scheduleBarriesList);
                                }
                            } catch (JSONException je) {

                            }
                        } else {
                            try {
                                assert response.errorBody() != null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                                utilities.cancelProgressDialog();
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        utilities.cancelProgressDialog();
                    }
                });
    }

    private void setRecyclerView(ArrayList<ScheduleModel> scheduleBarriesList) {
         adapter = new ScheduleAdapter(scheduleBarriesList, Constant.RV_SCHEDULE_BARRIER_LIST,
                getActivity(), refersh);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        recycler_view.setNestedScrollingEnabled(true);
        adapter.notifyDataSetChanged();
    }

    Refersh refersh = new Refersh() {
        @Override
        public void pageRefersh() {
           // getScheduleList(fro);
        }
    };

    public interface Refersh {

        void pageRefersh();

    }

}
