package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardMenuActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.DateConvertor;
import in.varadhismartek.patashalaerp.GeneralClass.RingProgressBar;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.StudentModule.StudentModel;
import in.varadhismartek.patashalaerp.StudentModule.StudentModuleActivity;
import in.varadhismartek.patashalaerp.StudentModule.StudentRecyclerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveDashboardFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    RecyclerView recycler_view;
    CardView cv_apply, cv_statement, cv_adminList;
    RingProgressBar progressBar;
    TextView tv_usedCount, tv_totalCount,tv_statement,noRecords;
    ArrayList<LeaveModel> list;


    LeaveAdapter adapter;
    private ArrayList<LeaveModel> leaveList;
    APIService mApiService;

    ArrayList<LeaveModel> arrayListForChart;
    FloatingActionButton fab_add;
    RelativeLayout rl_fabMain;
    LinearLayout llStaffLeave,llStudentLeave;
    Spinner spUserType;
    ArrayList<LeaveModel> leaveModels;
    ArrayList<EmpLeaveModel> empLeaveList;
    ArrayList<EmpLeaveModel> empLeaveListNew;
    ArrayList<EmpLeaveModel> employeeList;
    DateConvertor convertor;
    ProgressDialog mProgressDialog;


    String Student_ID;
    ArrayList<StudentModel> studentLeaveList = new ArrayList<>();

    String student_name = "", student_middle_name = "", student_last_name = "", StrName;

    public LeaveDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_dashboard, container, false);

        initViews(view);
        initListeners();
        setRecyclerView();
        getEmployeeAllLeaveDetailsAPI();
        //getLeaveStatementAPI();
        spinnerUserType();

        return view;
    }

    private void spinnerUserType() {
        assert getActivity() != null;

       final DashBoardMenuActivity leaveActivity = (DashBoardMenuActivity) getActivity();

        ArrayList<String> userType = new ArrayList<>();
        userType.add("-Leave-");
        userType.add("Staff");
        userType.add("Student");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),userType);
        spUserType.setAdapter(customSpinnerAdapter);


        spUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==1){

                    getEmployeeAllLeaveDetailsAPI();


                    // leaveActivity.loadFragment(Constant.LEAVE_ADMIN_LIST_FRAGMENT, null);

                }
                else if(position==2){

                    getStudentLeaveDetailsApi();

                    //leaveActivity.loadFragment(Constant.ALL_STUDENT_LEAVE, null);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getStudentLeaveDetailsApi() {


        mProgressDialog.setMessage("Laoding");
        mProgressDialog.show();
        mApiService.getAllStudentLeave(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("STU_LEAVE_DATA_1", "" + response.body());
                if (response.isSuccessful()) {

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message = object.getString("message");

                        JSONObject jsonData = object.getJSONObject("data");
                        Log.i("STU_LEAVE_DATA", "" + jsonData.toString());
                        Iterator<String> key = jsonData.keys();
                        while (key.hasNext()) {

                            String myDivisionKey = key.next();
                            Log.d("STU_LEAVE_DATA_Value", myDivisionKey);

                            JSONObject jsonObjectValue = jsonData.getJSONObject(myDivisionKey);
                            Log.d("STU_LEAVE_DATA_Value_1", jsonObjectValue.toString());

                            if (!jsonObjectValue.isNull("student_first_name")) {
                                student_name = jsonObjectValue.getString("student_first_name");
                            }


                            if (!jsonObjectValue.isNull("student_last_name")) {
                                student_last_name = jsonObjectValue.getString("student_last_name");

                            }

                            StrName = student_name + " " + student_middle_name + " " + student_last_name;

                            Log.i("STU_LEAVE_DATA_StrName", "" + StrName);

                            String strclass = jsonObjectValue.getString("class");
                            String strsection = jsonObjectValue.getString("section");





                            studentLeaveList.add(new StudentModel(
                                    jsonObjectValue.getString("leave_type"),
                                    jsonObjectValue.getString("subject"),
                                    jsonObjectValue.getString("to_date"),
                                    jsonObjectValue.getString("from_date"),
                                    jsonObjectValue.getString("message"),
                                    jsonObjectValue.getString("section"),
                                    jsonObjectValue.getString("student_uuid"),
                                    jsonObjectValue.getString("class"),
                                    StrName
                            ));


                        }
                        if (studentLeaveList.size() > 0) {
                            noRecords.setVisibility(View.GONE);
                            recycler_view.setVisibility(View.VISIBLE);
                            setStudentRecyclerView(studentLeaveList);
                            mProgressDialog.dismiss();
                        } else {
                            noRecords.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                            mProgressDialog.dismiss();

                        }

                    } catch (JSONException je) {
                        mProgressDialog.dismiss();

                    }
                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        mProgressDialog.dismiss();

                    }catch (Exception je){
                        je.printStackTrace();
                        mProgressDialog.dismiss();

                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProgressDialog.dismiss();


            }
        });

    }

    private void setStudentRecyclerView(ArrayList<StudentModel> studentLeaveList) {

        StudentRecyclerAdapter studentRecyclerAdapter = new StudentRecyclerAdapter(getActivity(), studentLeaveList,
                Constant.RV_ALL_STUDENT_LEAVE_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(studentRecyclerAdapter);
        studentRecyclerAdapter.notifyDataSetChanged();


    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        cv_apply.setOnClickListener(this);
        cv_statement.setOnClickListener(this);
        cv_adminList.setOnClickListener(this);

    }

    private void initViews(View view) {

        convertor = new DateConvertor();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        cv_apply = view.findViewById(R.id.cv_apply);
        cv_statement = view.findViewById(R.id.cv_statement);
        tv_statement = view.findViewById(R.id.tv_statement);
        recycler_view = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        tv_totalCount = view.findViewById(R.id.tv_totalCount);
        tv_usedCount = view.findViewById(R.id.tv_usedCount);
        cv_adminList = view.findViewById(R.id.cv_adminList);
        noRecords = view.findViewById(R.id.noRecords);
        rl_fabMain = view.findViewById(R.id.rl_fabMain);
        fab_add = view.findViewById(R.id.fab_add);
        llStaffLeave = view.findViewById(R.id.llStaffLeave);
        llStudentLeave = view.findViewById(R.id.llStudentLeave);
        spUserType = view.findViewById(R.id.spUserType);
        tv_statement.setText("Staff Leave");
        mProgressDialog = new ProgressDialog(getActivity());
        mApiService = ApiUtils.getAPIService();
        leaveList = new ArrayList<>();
        arrayListForChart = new ArrayList<>();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rl_fabMain.getVisibility() == View.VISIBLE) {
                    rl_fabMain.setVisibility(View.GONE);
                    //floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_sad));

                    fab_add.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

                } else {
                    rl_fabMain.setVisibility(View.VISIBLE);
                    fab_add.setImageDrawable(getResources().getDrawable(R.drawable.close_white));
                }

            }
        });

        empLeaveList = new ArrayList<>();
        leaveModels = new ArrayList<>();
        employeeList = new ArrayList<>();
        empLeaveListNew = new ArrayList<>();


    }

    private void setRecyclerView() {
        if (leaveList.size() > 0) {
            noRecords.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
            adapter = new LeaveAdapter(leaveList, getActivity(), Constant.RV_LEAVE_INBOX_LIST_UPPER);
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            noRecords.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        assert getActivity() != null;

        DashBoardMenuActivity leaveActivity = (DashBoardMenuActivity) getActivity();

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().finish();

                break;

            case R.id.cv_apply:
                leaveActivity.loadFragment(Constant.LEAVE_APPLY_FRAGMENT, null);
                break;

            case R.id.cv_statement://staff request leave
                leaveActivity.loadFragment(Constant.LEAVE_ADMIN_LIST_FRAGMENT, null);

                break;

            case R.id.cv_adminList://student request leave
                leaveActivity.loadFragment(Constant.ALL_STUDENT_LEAVE, null);

                break;


        }

    }

    private void getEmployeeAllLeaveDetailsAPI(){

        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        if (empLeaveList.size() > 0)
            empLeaveList.clear();

        mApiService.getAllEmployeeLeaveDetails(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        leaveModels.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = object.getString("status");

                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("ADMIN_API_DATA", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()) {

                                String myKey = key.next();
                                Log.d("ADMIN_KEY", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("ADMIN_API_KEY_DATA", keyData.toString());

                                String employee_name = keyData.getString("employee_name");
                                String applied_datetime = keyData.getString("applied_datetime");
                                String from_date = keyData.getString("from_date");
                                String employee_photo = keyData.getString("employee_photo");
                                String leave_status = keyData.getString("leave_status");
                                String leave_type = keyData.getString("leave_type");
                                String employee_uuid = keyData.getString("employee_uuid");
                                String to_date = keyData.getString("to_date");
                                String school_id = keyData.getString("school_id");
                                String subject = keyData.getString("subject");
                                String leave_id = keyData.getString("leave_id");

                                String appliedDate = convertor.getDateByTZ_SSS(applied_datetime);
                                String fromDate = convertor.getDateByTZ(from_date);
                                String toDate = convertor.getDateByTZ(to_date);

                                if (empLeaveList.size() > 0) {

                                    boolean b = true;

                                    for (int i = 0; i < empLeaveList.size(); i++) {

                                        if (empLeaveList.get(i).getAppliedDate().equals(appliedDate)) {

                                            b = false;

                                            empLeaveList.get(i).getLeaveList().add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                                    leave_type, employee_uuid, toDate, school_id, subject, leave_id));
                                        }

                                    }

                                    if (b) {

                                        list = new ArrayList<>();
                                        list.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                                leave_type, employee_uuid, toDate, school_id, subject, leave_id));

                                        empLeaveList.add(new EmpLeaveModel(appliedDate, list));
                                    }

                                } else {
                                    list = new ArrayList<>();
                                    list.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                            leave_type, employee_uuid, toDate, school_id, subject, leave_id));

                                    empLeaveList.add(new EmpLeaveModel(appliedDate, list));

                                }

                                leaveModels.add(new LeaveModel(employee_name, fromDate, employee_photo, leave_status,
                                        leave_type, employee_uuid, toDate, school_id, subject, leave_id));
                            }

                            setRecyclerViewList(leaveModels);
                            adapter.notifyDataSetChanged();
                            noRecords.setVisibility(View.GONE);
                            mProgressDialog.dismiss();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        noRecords.setVisibility(View.VISIBLE);
                        mProgressDialog.dismiss();
                    }


                } else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        noRecords.setVisibility(View.VISIBLE);
                        mProgressDialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                        noRecords.setVisibility(View.VISIBLE);
                        mProgressDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                noRecords.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();


            }
        });

    }

    private void setRecyclerViewList(ArrayList<LeaveModel> leaveModels) {
        Gson gson = new Gson();
        System.out.println("gson*****" + gson.toJson(leaveModels));
        adapter = new LeaveAdapter(getActivity(), leaveModels, Constant.RV_LEAVE_ADMIN_LIST);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        recycler_view.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    private void getLeaveStatementAPI(){

        mApiService.getEmployeeLeaveStatement(/*"55e9cd8c-052a-46b1-b81c-14f85e11a8fe","c91ce329-df26-46a6-902b-6b9a92920003"*/
                Constant.SCHOOL_ID,Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));

                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equalsIgnoreCase("Success")){

                            JSONObject jsonData = object.getJSONObject("data");
                            Log.d("Statement_data", jsonData.toString());

                            Iterator<String> key = jsonData.keys();

                            while (key.hasNext()){

                                String myKey = key.next();
                                Log.d("Statement_Key", myKey);

                                JSONObject keyData = jsonData.getJSONObject(myKey);
                                Log.d("Statement_Key_data", keyData.toString());

                                String leave_type = keyData.getString("leave_type");
                                String total_leaves = keyData.getString("total_leaves");
                                String used_leaves = keyData.getString("used_leaves");
                                String remaining_leaves = keyData.getString("remaining_leaves");

                                arrayListForChart.add(new LeaveModel(leave_type,total_leaves,used_leaves,remaining_leaves));
                            }

                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            Log.d("Satetment_api_f", String.valueOf(response.code()));

                            setRing(arrayListForChart);

                        }else {

                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

        for (int i=0; i<arrayList.size(); i++){

            total = total + Integer.parseInt(arrayList.get(i).getTotalLeave());
            used = used + Integer.parseInt(arrayList.get(0).getUsedLeave());
        }

        setRingProcess(total,used);
    }
    private void setRingProcess(int total, final int used){

        if (total==0){
            progressBar.setMax(1);
        }else
            progressBar.setMax(total);

        progressBar.setRingColor(Color.parseColor("#52b155"));
        progressBar.setRingProgressColor(Color.parseColor("#ff0000"));
        progressBar.setTextSize(50);

        tv_totalCount.setText(total+"");
        tv_usedCount.setText(used+"");

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
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0){

                if(progress < max){
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
