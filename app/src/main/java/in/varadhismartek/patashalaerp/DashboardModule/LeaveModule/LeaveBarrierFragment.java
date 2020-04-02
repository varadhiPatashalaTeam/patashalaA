package in.varadhismartek.patashalaerp.DashboardModule.LeaveModule;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeWorkActivity;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveBarrierFragment extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn, iv_add, iv_next_page;
    EditText et_leave_type;
    RecyclerView recycler_view;
    LeaveAdapter adapter;
    ProgressDialog progressDialog;

    APIService mApiService;

    ArrayList<LeaveModel> leaveTypeList;

    public LeaveBarrierFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_barrier, container, false);

        initViews(view);
        initListeners();
        getLeaveBarrierApi();
        setRecyclerView();


        return view;



    }

    public void setRecyclerView() {

        adapter = new LeaveAdapter(leaveTypeList, getActivity(), Constant.RV_LEAVE_BARRIER, refereshName);
        recycler_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        iv_next_page.setOnClickListener(this);

    }

    private void initViews(View view) {

        mApiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_next_page = view.findViewById(R.id.iv_next_page);
        et_leave_type = view.findViewById(R.id.et_leave_type);
        iv_add = view.findViewById(R.id.iv_add);
        recycler_view = view.findViewById(R.id.recycler_view);

        leaveTypeList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {

        assert getActivity()!=null;

        switch (view.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();

                break;

            case R.id.iv_add:
                String str_type = et_leave_type.getText().toString();

                if (str_type.equals(""))
                    Toast.makeText(getActivity(), "Leave Type Required", Toast.LENGTH_SHORT).show();

                else {
                    addLeaveBarrierApi(str_type);

                }

                break;

            case R.id.iv_next_page:

                HomeWorkActivity leaveActivity = (HomeWorkActivity) getActivity();
                leaveActivity.loadFragment(Constant.LEAVE_SCHOOL_BARRIER_FRAGMENT, null);

                break;
        }
    }

    private void getLeaveBarrierApi() {

        leaveTypeList.clear();
       // progressDialog.show();
       // progressDialog.setMessage("Please Wait");
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

                                Log.d("LEAVE_BARRIER_KEY_VALUE",leave_type+" "+leave_id+" "+status);

                                leaveTypeList.add(new LeaveModel(leave_type, leave_id, status));
                                adapter.notifyDataSetChanged();
                            }

                        //    progressDialog.dismiss();

                        }else {

                            Toast.makeText(getActivity(), "Data Fetching Error", Toast.LENGTH_SHORT).show();
                         //   progressDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("LEAVE_BARRIER_FAIL", String.valueOf(response.code()));

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.d("LEAVE_BARRIER_EXCEPTION",t.getMessage());
                progressDialog.dismiss();

            }
        });
    }

    private void addLeaveBarrierApi(String leaveType){

       // {"leaves":{"1":{"name":"Sick Leave","status":"true"}}}

        JSONObject obj = new JSONObject();
        JSONObject objCount = new JSONObject();
        JSONObject objLeave = new JSONObject();

        try {

            obj.put("name",leaveType);
            obj.put("status","true");

            objCount.put("1", obj);
            objLeave.put("leaves", objCount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("MY_JSON_OBJ", objLeave.toString());

        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        mApiService.addLeaveBarrier(Constant.SCHOOL_ID, objLeave,Constant.EMPLOYEE_BY_ID/* Constant.FIRST_NAME, Constant.LAST_NAME,
                Constant.MOBILE_NO,Constant.AADHAR_NO*/).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    Log.d("MY_JSON_OBJ", response.body().toString());

                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("Success")){

                            Toast.makeText(getActivity(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            getLeaveBarrierApi();
                            et_leave_type.setText("");

                        }else {


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();

                }else {
                    Toast.makeText(getActivity(), "Data Is Invalid", Toast.LENGTH_SHORT).show();
                    Log.d("MY_JSON_OBJ", String.valueOf(response.code()));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("MY_JSON_OBJ", t.getMessage());

            }
        });


    }

    RefereshName refereshName = new RefereshName() {
        @Override
        public void refresh() {

            getLeaveBarrierApi();
        }
    };
}
