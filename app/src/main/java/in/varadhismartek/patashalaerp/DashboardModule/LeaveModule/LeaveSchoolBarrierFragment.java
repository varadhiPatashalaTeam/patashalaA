package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveSchoolBarrierFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn, iv_next_page, iv_add;
    Spinner spn_leaveType;
    EditText et_leaveCode, et_leaveNum, et_noticePeriod, et_applyTimes;
    RecyclerView recycler_view;

    LeaveAdapter adapter;

    APIService mApiService;
    ProgressDialog progressDialog;

    ArrayList<String> spnList;
    ArrayList<LeaveModel> leaveList;
    ArrayList<LeaveModel> leaveTypeList;


    String str_leaveType = "", str_leaveId = "";

    public LeaveSchoolBarrierFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_school_barrier, container, false);

        initViews(view);
        initListeners();
        setSpinnerForLeave();
        getLeaveBarrierApi();
        getSchoolLeaveBarrierDetailsAPI();
        setRecyclerView();


        return view;
    }

    private void setRecyclerView() {

        adapter = new LeaveAdapter(leaveList,getActivity(), Constant.RV_SCHOOL_LEAVE_BARRIER, refereshName);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void setSpinnerForLeave(){

        spnList.add(0, "-Select Leave-");

        CustomSpinnerAdapter spnAdapter = new CustomSpinnerAdapter(getActivity(), spnList);
        spn_leaveType.setAdapter(spnAdapter);
        spnAdapter.notifyDataSetChanged();

        spn_leaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){
                    str_leaveType = "";
                    str_leaveId = "";

                }else {
                    str_leaveType = spn_leaveType.getSelectedItem().toString();
                    str_leaveId = leaveTypeList.get(i-1).getLeaveID();
                }

                et_leaveCode.setText("");
                et_leaveNum.setText("");
                et_noticePeriod.setText("");
                et_applyTimes.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        iv_next_page.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_next_page = view.findViewById(R.id.iv_next_page);
        iv_add = view.findViewById(R.id.iv_add);
        spn_leaveType = view.findViewById(R.id.spn_leaveType);
        et_leaveCode = view.findViewById(R.id.et_leaveCode);
        et_leaveNum = view.findViewById(R.id.et_leaveNum);
        et_noticePeriod = view.findViewById(R.id.et_noticePeriod);
        et_applyTimes = view.findViewById(R.id.et_applyTimes);
        recycler_view = view.findViewById(R.id.recycler_view);

        spnList = new ArrayList<>();
        leaveList = new ArrayList<>();
        leaveTypeList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.iv_next_page:

                break;

            case R.id.iv_add:

                String leaveCode    = et_leaveCode.getText().toString();
                String noOfLeave    = et_leaveNum.getText().toString();
                String noticePeriod = et_noticePeriod.getText().toString();
                String availability = et_applyTimes.getText().toString();

                if (str_leaveType.equalsIgnoreCase(""))
                    Toast.makeText(getActivity(), "Leave Type Is Required", Toast.LENGTH_SHORT).show();

                else if (leaveCode.equals(""))
                    Toast.makeText(getActivity(), "Leave Code Is Required", Toast.LENGTH_SHORT).show();

                else if (noOfLeave.equals(""))
                    Toast.makeText(getActivity(), "Leave/Year Is Required", Toast.LENGTH_SHORT).show();

                else if (noticePeriod.equals(""))
                    Toast.makeText(getActivity(), "Notice Period Is Required", Toast.LENGTH_SHORT).show();

                else if (availability.equals(""))
                    Toast.makeText(getActivity(), "Availability Is Required", Toast.LENGTH_SHORT).show();

                else {

                    Log.d("MY_API", "else block");
                    Log.d("MY_API_INPUT", Constant.SCHOOL_ID+" "+leaveCode+" "+noOfLeave+" "+noticePeriod+" "
                            +Constant.FIRST_NAME+" "+ Constant.LAST_NAME+" "+Constant.MOBILE_NO+" "+
                            Constant.AADHAR_NO+" "+str_leaveId+" " +str_leaveType+" "+availability);
                    Log.d("MY_API_INPUT", str_leaveId);



                    mApiService.addSchoolLeaveBarrierDetails(Constant.SCHOOL_ID,leaveCode,noOfLeave,noticePeriod,
                            Constant.EMPLOYEE_BY_ID, str_leaveId,str_leaveType,availability).
                            enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()){

                                try {
                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("Success")){
                                        Toast.makeText(getActivity(), "Added Successfully ", Toast.LENGTH_SHORT).show();
                                        Log.d("MY_API_succ", response.body().toString());

                                        getSchoolLeaveBarrierDetailsAPI();

                                        et_leaveCode.setText("");
                                        et_leaveNum.setText("");
                                        et_noticePeriod.setText("");
                                        et_applyTimes.setText("");

                                    }else {
                                        Toast.makeText(getActivity(), "Leave Type Not Exists", Toast.LENGTH_SHORT).show();
                                        Log.d("MY_API_succ", response.body().toString());

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else {

                                Toast.makeText(getActivity(), "Leave Type Not Exists", Toast.LENGTH_SHORT).show();
                                Log.d("MY_API_FAIL", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.d("MY_API_EX",t.getMessage());
                        }
                    });

                }

                break;
        }
    }

    private void getLeaveBarrierApi() {

        spnList.clear();
        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        mApiService.getLeaveBarrier(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    Log.d("LEAVE_BARRIER_SUCCESS",response.body().toString());

                    JSONObject obj = null;

                    try {
                        obj = new JSONObject(new Gson().toJson(response.body()));

                        Log.d("LEAVE_BARRIER_OBJECT",obj.toString());
                        String status1 = obj.getString("status");

                        if (status1.equalsIgnoreCase("Success")){

                            JSONObject jsonData = obj.getJSONObject("data");
                            Log.d("LEAVE_BARRIER_DATA",jsonData.toString());

                            Iterator key = jsonData.keys();

                            while (key.hasNext()){

                                String myKey = key.next().toString();
                                Log.d("LEAVE_BARRIER_KEY",myKey);

                                JSONObject jsonKeyData = jsonData.getJSONObject(myKey);
                                Log.d("LEAVE_BARRIER_KEY_DATA",jsonKeyData.toString());

                                String leave_type = jsonKeyData.getString("leave_type");
                                String leave_id = jsonKeyData.getString("leave_id");
                                boolean status = jsonKeyData.getBoolean("status");

                                Log.d("LEAVE_BARRIER_KEY_VALUE",leave_type+" "+status);

                                if (status) {
                                    spnList.add(leave_type);
                                    leaveTypeList.add(new LeaveModel(leave_type, leave_id, status));
                                }
                            }

                            setSpinnerForLeave();

                            progressDialog.dismiss();

                        }else {

                            Toast.makeText(getActivity(), "Data Fetching Error", Toast.LENGTH_SHORT).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("LEAVE_BARRIER_FAIL",response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LEAVE_BARRIER_EXCEPTION",t.getMessage());

            }
        });
    }

    private void getSchoolLeaveBarrierDetailsAPI(){

        leaveList.clear();
        mApiService.getSchoolLeaveBarrierDetails(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){

                    try {
                        JSONObject obj = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("SCHOOL_LEAVE_OBJ", obj.toString());

                        JSONObject jsonData = obj.getJSONObject("data");
                        Log.d("SCHOOL_LEAVE_DATA", jsonData.toString());

                        Iterator<String> key = jsonData.keys();

                        while (key.hasNext()){

                            String myKey = key.next();
                            Log.d("SCHOOL_LEAVE_KEY", myKey);

                            JSONObject keyData = jsonData.getJSONObject(myKey);
                            Log.d("SCHOOL_LEAVE_keyData", keyData.toString());

                            String leave_type = keyData.getString("leave_type");
                            String leave_code = keyData.getString("leave_code");
                            String leaves_per_year = keyData.getString("leaves_per_year");
                            String notice_period = keyData.getString("notice_period");
                            String availability = keyData.getString("availability");
                            String status = keyData.getString("status");

                            leaveList.add(new LeaveModel(leave_type,leave_code,leaves_per_year,notice_period,availability, status,""));
                            adapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    RefereshName refereshName = new RefereshName() {
        @Override
        public void refresh() {
            getSchoolLeaveBarrierDetailsAPI();
        }
    };
}
