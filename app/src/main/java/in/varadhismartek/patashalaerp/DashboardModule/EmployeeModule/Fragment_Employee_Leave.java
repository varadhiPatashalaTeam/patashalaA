package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.LeaveModule.LeaveModel;
import in.varadhismartek.patashalaerp.GeneralClass.RingProgressBar;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Employee_Leave extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    RecyclerView recycler_view;
    CardView cv_apply, cv_statement, cv_adminList;
    RingProgressBar progressBar;
    TextView tv_usedCount, tv_totalCount;
    String employeeID = "";

    EmployeeAdapter adapter;
    private ArrayList<LeaveModel> leaveList;
    APIService mApiService;

    ArrayList<LeaveModel> arrayListForChart;
    CardView cardViewLeave;
    LinearLayout linearLayoutLeave;
    TextView noRecords;

    public Fragment_Employee_Leave() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_dashboard, container, false);

        initViews(view);
        initListeners();
        getBundle();
        //     setRecyclerView();
        getEmployeeAllLeaveDetailsAPI();
        getLeaveStatementAPI();

        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();

        employeeID = bundle.getString("EMPUUID");
        Constant.EMPLOYEE_ID = employeeID;
        Log.i("Constant*EMPLOYEE_ID1", Constant.EMPLOYEE_ID);


    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        cv_apply.setOnClickListener(this);
        cv_statement.setOnClickListener(this);
        cv_adminList.setOnClickListener(this);
    }

    private void initViews(View view) {

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        noRecords = view.findViewById(R.id.noRecords);
        cv_apply = view.findViewById(R.id.cv_apply);
        cv_statement = view.findViewById(R.id.cv_statement);
        recycler_view = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        tv_totalCount = view.findViewById(R.id.tv_totalCount);
        tv_usedCount = view.findViewById(R.id.tv_usedCount);
        cv_adminList = view.findViewById(R.id.cv_adminList);
        cardViewLeave = view.findViewById(R.id.leavecardView);
        linearLayoutLeave = view.findViewById(R.id.lleave);
        cardViewLeave.setVisibility(View.GONE);
        linearLayoutLeave.setVisibility(View.GONE);
        mApiService = ApiUtils.getAPIService();
        leaveList = new ArrayList<>();
        leaveList.clear();
        arrayListForChart = new ArrayList<>();

    }


    @Override
    public void onClick(View view) {

        assert getActivity() != null;

        //   DashBoardMenuActivity leaveActivity = (DashBoardMenuActivity) getActivity();

        switch (view.getId()) {

            case R.id.iv_backBtn:
                getActivity().onBackPressed();

                break;

          /*  case R.id.cv_apply:
                leaveActivity.loadFragment(Constant.LEAVE_APPLY_FRAGMENT, null);
                break;

            case R.id.cv_statement:
                leaveActivity.loadFragment(Constant.LEAVE_STATEMENT_FRAGMENT, null);

                break;*/

            case R.id.cv_adminList:
                //   leaveActivity.loadFragment(Constant.LEAVE_ADMIN_LIST_FRAGMENT, null);

                break;
        }

    }

    private void getEmployeeAllLeaveDetailsAPI() {

        mApiService.getEmployeeAllLeaveDetails(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                JSONObject obj = null;

                if (response.isSuccessful()) {

                    Log.d("MY_API_SUCCESS", response.body().toString());

                    try {
                        recycler_view.setVisibility(View.VISIBLE);
                        obj = new JSONObject(new Gson().toJson(response.body()));
                        String status = obj.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = obj.getJSONObject("data");
                            Log.d("MY_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("MY_API_KEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);


                                String leave_status = keyData.getString("leave_status");
                                String applied_datetime = keyData.getString("applied_datetime");
                                String from_date = keyData.getString("from_date");
                                String leave_id = keyData.getString("leave_id");
                                String leave_type = keyData.getString("leave_type");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String to_date = keyData.getString("to_date");
                                String school_id = keyData.getString("school_id");
                                String subject = keyData.getString("subject");

                                Log.d("MY_API_F_DATA", leave_status + " " + applied_datetime + " " + from_date + " " + leave_id + " "
                                        + leave_type + " " + employee_uuid + " " + to_date + " " + school_id + " " + subject);

                                String inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                                String inputFormat2 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                                String outputFormat = "dd-MM-yyyy";

                                SimpleDateFormat input = new SimpleDateFormat(inputFormat);
                                SimpleDateFormat input2 = new SimpleDateFormat(inputFormat2);
                                input.setTimeZone(TimeZone.getTimeZone("IST"));
                                SimpleDateFormat output = new SimpleDateFormat(outputFormat);
                                output.setTimeZone(TimeZone.getDefault());


                                try {

                                    Date applied = input.parse(applied_datetime);
                                    String appliedDate = output.format(applied);

                                    Date from = input2.parse(from_date);
                                    String fromDate = output.format(from);

                                    Date to = input2.parse(to_date);
                                    String toDate = output.format(to);

                                    Log.d("MY_DATE_APPLY", appliedDate);
                                    Log.d("MY_DATE_FROM", fromDate);
                                    Log.d("MY_DATE_TO", toDate);

                                    leaveList.add(new LeaveModel(leave_type, subject, appliedDate, fromDate, toDate, leave_status, leave_id, employee_uuid));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            setRecyclerView();
                            adapter.notifyDataSetChanged();

                        } else {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("MY_API_FAIL", String.valueOf(response.code()));

                    try {

                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("MY_API_FAIL", message);
                        Toast.makeText(getActivity(), "Leave Records not there", Toast.LENGTH_SHORT).show();
                        recycler_view.setVisibility(View.GONE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("MY_API_EX", t.getMessage());

            }
        });

    }

    private void setRecyclerView() {
        if (leaveList.size() > 0) {
            noRecords.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);

            adapter = new EmployeeAdapter(leaveList, getActivity(), Constant.RV_LEAVE_INBOX_LIST_UPPER);
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            noRecords.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);

        }
    }


    private void getLeaveStatementAPI() {

        mApiService.getEmployeeLeaveStatement(Constant.SCHOOL_ID, Constant.EMPLOYEE_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Log.d("Statement_data111", response.body() + "***" + response.code());
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("Statement_data", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("Statement_Key", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("Statement_Key_data", keyData.toString());

                                String leave_type = keyData.getString("leave_type");
                                String total_leaves = keyData.getString("total_leaves");
                                String used_leaves = keyData.getString("used_leaves");
                                String remaining_leaves = keyData.getString("remaining_leaves");

                                arrayListForChart.add(new LeaveModel(leave_type, total_leaves, used_leaves, remaining_leaves));
                            }

                           // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Log.d("Satetment_api_f", String.valueOf(response.code()));
//                            adapter.notifyDataSetChanged();

                            setRing(arrayListForChart);

                        } else {

                           // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            Log.d("Statement_api_status", message);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    try {
                        assert response.errorBody() != null;
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        String message = obj.getString("message");
                        Log.d("MY_STATUS", message);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("MY_STATUS", e.getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }

    private void setRing(ArrayList<LeaveModel> arrayList) {

        int total = 0;
        int used = 0;
        progress = 0;

        for (int i = 0; i < arrayList.size(); i++) {

            total = total + Integer.parseInt(arrayList.get(i).getTotalLeave());
            used = used + Integer.parseInt(arrayList.get(0).getUsedLeave());
        }

        setRingProcess(total, used);
    }

    private void setRingProcess(int total, final int used) {

        if (total == 0) {
            progressBar.setMax(1);
        } else
            progressBar.setMax(total);

        progressBar.setRingColor(Color.parseColor("#ff00bcd4"));
        progressBar.setRingProgressColor(Color.parseColor("#ff0000"));
        progressBar.setTextSize(50);

        tv_totalCount.setText(total + "");
        tv_usedCount.setText(used + "");

        max = total;

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < used; i++) {
                    try {
                        Thread.sleep(10);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    int max = 0;
    int progress = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {

                if (progress < max) {
                    progress++;
                    initProgressBar(progress);
                }
            }
        }
    };

    private void initProgressBar(int progress) {

        progressBar.setProgress(progress);
        progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {

            }
        });

    }


}