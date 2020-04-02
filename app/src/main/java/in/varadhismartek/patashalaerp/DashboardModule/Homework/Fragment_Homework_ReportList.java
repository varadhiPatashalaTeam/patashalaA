package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Homework_ReportList extends Fragment implements View.OnClickListener {
    String HomeWork_UUID = "",HomeWork_Title="",HomeWork_Desc;
    ImageView iv_backBtn;
    TextView text_view_assign, tv_fromDate,tv_homeworkTitle,tv_homeworkDesc;
    APIService apiService;
    RecyclerView recycler_view;
int count=0;
    ArrayList<HomeworkModel> homeworkreportModels;
    String student_last_name = "", homework_title = "", student_birth_certificate_number = "", roll_no = "",
            student_first_name = "", student_photo = "", homework_id = "", completion_percentage = "",
            start_date = "", description = "", end_date = "", student_id = "", student_dob = "";
Utilities utilities;
    public Fragment_Homework_ReportList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__homework_report_inbox, container, false);

        initView(view);
        initListeners();
        getStringBundle();
        getHomeWorkReportAPI();

        return view;

    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
    }

    private void getHomeWorkReportAPI() {
        utilities.displayProgressDialog(getActivity(),"processing***",false);
        Log.i("homeworkId",""+HomeWork_UUID);
       // apiService.get_particular_homework_report_details("55e9cd8c-052a-46b1-b81c-14f85e11a8fe", "855d5c90-1c4c-42dd-bf3a-19e5d448317c").enqueue(new Callback<Object>() {
        apiService.get_particular_homework_report_details(Constant.SCHOOL_ID, HomeWork_UUID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (response.isSuccessful()) {
                    utilities.cancelProgressDialog();
                    try {
                        homeworkreportModels.clear();
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        System.out.println("MEssage**C*" + object.getString("message"));
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject = object.getJSONObject("data");

                            Iterator<String> keys = jsonObject.keys();


                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject objectData = jsonObject.getJSONObject(key);

                                System.out.println("completePercentage***1 " + objectData);


                                if (objectData.isNull("student_last_name")) {
                                    student_last_name = "";
                                } else {
                                    student_last_name = objectData.getString("student_last_name");
                                }

                                if (objectData.isNull("homework_title")) {
                                    homework_title = "";
                                } else {
                                    homework_title = objectData.getString("homework_title");
                                }
                                if (objectData.isNull("student_birth_certificate_number")) {
                                    student_birth_certificate_number = "";
                                } else {
                                    student_birth_certificate_number = objectData.getString("student_birth_certificate_number");
                                }

                                if (objectData.isNull("roll_no")) {
                                    roll_no = "";
                                } else {
                                    roll_no = objectData.getString("roll_no");
                                }

                                if (objectData.isNull("student_first_name")) {
                                    student_first_name = "";
                                } else {
                                    student_first_name = objectData.getString("student_first_name");
                                }

                                if (objectData.isNull("student_photo")) {
                                    student_photo = "";
                                } else {
                                    student_photo = objectData.getString("student_photo");
                                }

                                if (objectData.isNull("homework_id")) {
                                    homework_id = "";
                                } else {
                                    homework_id = objectData.getString("homework_id");
                                }


                                if (objectData.isNull("completion_percentage")) {
                                    completion_percentage = "";
                                } else {
                                    completion_percentage = objectData.getString("completion_percentage");
                                }
                                if (objectData.isNull("start_date")) {
                                    start_date = "";
                                } else {
                                    start_date = objectData.getString("start_date");
                                }
                                if (objectData.isNull("description")) {
                                    description = "";
                                } else {
                                    description = objectData.getString("description");
                                }


                                if (objectData.isNull("end_date")) {
                                    end_date = "";
                                } else {
                                    end_date = objectData.getString("end_date");
                                }

                                if (objectData.isNull("student_id")) {
                                    student_id = "";
                                } else {
                                    student_id = objectData.getString("student_id");
                                }

                                if (objectData.isNull("student_dob")) {
                                    student_dob = "";
                                } else {
                                    student_dob = objectData.getString("student_dob");
                                }

count++;

                                homeworkreportModels.add(new HomeworkModel(
                                        count,student_last_name, homework_title, student_birth_certificate_number, roll_no,
                                        student_first_name, student_photo, homework_id, completion_percentage,
                                        start_date, description, end_date, student_id, student_dob));

                            }

                            setRecyclerView(homeworkreportModels);
                        }
                    } catch (JSONException je) {

                    }

                }else {
                    try {
                        assert response.errorBody()!=null;
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
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
    private void setRecyclerView(ArrayList<HomeworkModel> homeworkreportModels) {
        HomeworkAdapter adapter = new HomeworkAdapter(homeworkreportModels,Constant.RV_HOMEWORK_REPORT,getActivity());
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        text_view_assign = view.findViewById(R.id.text_view_assign);
        recycler_view = view.findViewById(R.id.recycler_view);
        tv_fromDate = view.findViewById(R.id.tv_fromDate);
        tv_homeworkTitle = view.findViewById(R.id.tv_homeworkTitle);
        tv_homeworkDesc = view.findViewById(R.id.tv_homeworkDesc);

        text_view_assign.setText("Homework Report List");

        tv_fromDate.setVisibility(View.GONE);

        homeworkreportModels = new ArrayList<>();

    }

    private void getStringBundle() {

        Bundle bundle = getArguments();
        if (bundle!= null){
            HomeWork_UUID = bundle.getString("HOMEWORK_UUID");
            HomeWork_Title = bundle.getString("HOMEWORK_TITLE");
            HomeWork_Desc= bundle.getString("HOMEWORK_DESC");
            Log.i("HomeWork_UUID**", "" + HomeWork_UUID+"*"+HomeWork_Desc);

            tv_homeworkTitle.setText(HomeWork_Title);
            tv_homeworkDesc.setText(HomeWork_Desc);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
        }
    }
}
