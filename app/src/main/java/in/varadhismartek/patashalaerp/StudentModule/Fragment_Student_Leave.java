package in.varadhismartek.patashalaerp.StudentModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Student_Leave extends Fragment {
    RecyclerView recyclerView;
    APIService apiService;
    String Student_ID;
    ArrayList<StudentModel> studentLeaveList = new ArrayList<>();

    String student_name = "", student_middle_name = "", student_last_name = "", StrName;

    TextView stuName,stuClass,stuSection,noRecords;
    ImageView iv_backBtn;
    TextView tvTitle;
    public Fragment_Student_Leave() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_leave_list, container, false);
        initView(view);
        getLeaveList();
        //setRecyclerView();
        return view;
    }

    private void getLeaveList() {

        apiService.getStudentLeave(/*Student_ID*/"75ed8265-369f-4954-a454-e5a9b6dd6352").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status1 = object.getString("status");
                        String message = object.getString("message");

                        JSONObject jsonData = object.getJSONObject("data");


                        Log.i("STU_LEAVE_DATA", "" + jsonData.toString());

                        student_name = jsonData.getString("student_first_name");
                        student_last_name = jsonData.getString("student_last_name");

/*
                        if (!jsonData.isNull("student_first_name")) {
                        }


                        if (!jsonData.isNull("student_last_name")) {

                        }*/


                        StrName = student_name +" " + student_last_name;

                        Log.i("STU_LEAVE_DATA_StrName", "" + StrName);

                        String strclass = jsonData.getString("class");
                        String strsection = jsonData.getString("section");
                        String total_leaves = jsonData.getString("total_leaves");


                        stuName.setText("Student Name : "+StrName);
                        stuClass.setText("Class : "+strclass);
                        stuSection.setText("Section : "+strsection);

                        stuName.setVisibility(View.GONE);
                        stuClass.setVisibility(View.GONE);
                        stuSection.setVisibility(View.GONE);

                        if (!jsonData.isNull("leave_details")) {
                            studentLeaveList.clear();

                            JSONObject jsonLeaveData = jsonData.getJSONObject("leave_details");
                            Iterator<String> key = jsonLeaveData.keys();

                            while (key.hasNext()) {

                                String myDivisionKey = key.next();
                                Log.d("STU_LEAVE_DATA_Value", myDivisionKey);

                                JSONObject jsonObjectValue = jsonLeaveData.getJSONObject(myDivisionKey);


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

                         //   setRecyclerView();
                            if(studentLeaveList.size()>0) {
                                noRecords.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                setRecyclerView(studentLeaveList);
                            }else {
                                noRecords.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);


                            }


                        } else {
                            studentLeaveList.clear();
                        }


                    } catch (JSONException je) {

                    }
                }
                else {
                    try {
                        assert response.errorBody() != null;
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        noRecords.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }catch (Exception je){
                        je.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setRecyclerView(ArrayList<StudentModel> studentLeaveList) {

            StudentRecyclerAdapter studentRecyclerAdapter = new StudentRecyclerAdapter(getActivity(),studentLeaveList,
                    Constant.RV_STUDENT_LEAVE_LIST);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(studentRecyclerAdapter);
            studentRecyclerAdapter.notifyDataSetChanged();



    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        recyclerView = view.findViewById(R.id.recyclerView);
        stuName = view.findViewById(R.id.tvname);
        stuClass = view.findViewById(R.id.tvclass);
        stuSection = view.findViewById(R.id.tvsection);
         tvTitle = view.findViewById(R.id.tvTitle);
        noRecords = view.findViewById(R.id.noRecords);
        tvTitle.setGravity(Gravity.START);
        tvTitle.setText("Student Leave");

        Bundle bundle = getArguments();
        if (bundle != null) {
            Student_ID = bundle.getString("STUDENTID");
        }
Log.i("Student_ID****",""+Student_ID);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }


}
