package in.varadhismartek.patashalaerp.DashboardModule.EmployeeModule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkAdapter;
import in.varadhismartek.patashalaerp.DashboardModule.Homework.HomeworkModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Homework_Employee extends Fragment implements View.OnClickListener {
    String EMPID = "", currentDate = "", str_toDate = "", endYear = "";
    private RecyclerView recycler_View;
    String homeworkId, homeworkTitle, description, homeClass, section, subject, startDate, endDate;
    APIService apiService;
    ArrayList<HomeworkModel> homeworkModelsInbox, homeworkModelsInboxFilter;
    Calendar calendar;
    Date date1;
    TextView tvTitle, tvTitleDate,tvNorecords;
    EmployeeAdapter homeworkAdapter;
    private int year, month, date;
    private ImageView iv_backBtn;

    public Fragment_Homework_Employee() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_homework, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView(view);
        initListener();
        getEmployeeHomework();
        return view;

    }

    private void initListener() {
        tvTitleDate.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
    }


    private void initView(View view) {
        Bundle b = getArguments();
        if (b != null) {
            EMPID = b.getString("EMPUUID");


        }
        apiService = ApiUtils.getAPIService();
        recycler_View = view.findViewById(R.id.recycler_View);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        tvNorecords = view.findViewById(R.id.tvNorecords);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitleDate = view.findViewById(R.id.tvTitle2);

        tvTitle.setText("Employee Homework");

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());

        tvTitleDate.setText(currentDate);


        homeworkModelsInbox = new ArrayList<>();
        homeworkModelsInboxFilter = new ArrayList<>();
        homeworkModelsInbox.clear();
        homeworkModelsInboxFilter.clear();

    }

    private void getEmployeeHomework() {
       // EMPID = "cdfab2cd-9a3c-4e5e-9810-258f693bde93";
        apiService.get_teacher_homework_list(Constant.SCHOOL_ID, EMPID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            try {
                                Log.i("homeinboxSChool**", "" + response.body() + "*****" + response.code());

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONArray jsonArray = object.getJSONArray("datadict");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objectData = jsonArray.getJSONObject(i);


                                        if (objectData.isNull("homework_uuid")) {
                                            homeworkId = "";
                                        } else {
                                            homeworkId = objectData.getString("homework_uuid");
                                        }

                                        if (objectData.isNull("homework_title")) {
                                            homeworkTitle = "";
                                        } else {
                                            homeworkTitle = objectData.getString("homework_title");
                                        }
                                        if (objectData.isNull("description")) {
                                            description = "";
                                        } else {
                                            description = objectData.getString("description");
                                        }


                                        if (objectData.isNull("class")) {
                                            homeClass = "";
                                        } else {
                                            homeClass = objectData.getString("class");
                                        }
                                        if (objectData.isNull("section")) {
                                            section = "";
                                        } else {
                                            section = objectData.getString("section");
                                        }
                                        if (objectData.isNull("subject")) {
                                            subject = "";
                                        } else {
                                            subject = objectData.getString("subject");
                                        }
                                        if (objectData.isNull("start_date")) {
                                            startDate = "";
                                        } else {
                                            startDate = objectData.getString("start_date");
                                        }
                                        if (objectData.isNull("end_date")) {
                                            endDate = "";
                                        } else {
                                            endDate = objectData.getString("end_date");
                                        }


                                        homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                                homeClass, section, subject, startDate, endDate));

                                        Log.i("homeworkId**", "" + homeworkId + homeworkTitle);


                                    }


                                    setRecyclerView(homeworkModelsInbox);
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

    private void setRecyclerView(ArrayList<HomeworkModel> homeworkModelsInbox) {

        if (homeworkModelsInbox.size() > 0) {
            tvNorecords.setVisibility(View.GONE);
            recycler_View.setVisibility(View.VISIBLE);

            homeworkAdapter = new EmployeeAdapter(homeworkModelsInbox, getActivity(),
                    tvTitleDate.getText().toString(), Constant.RV_EMP_HOMEWORK_ROW);

            recycler_View.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_View.setAdapter(homeworkAdapter);
            homeworkAdapter.notifyDataSetChanged();
        } else {
            tvNorecords.setVisibility(View.VISIBLE);
            recycler_View.setVisibility(View.GONE);
            homeworkAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTitle2:

                openDateDialog();

                break;
                case R.id.iv_backBtn:

                getActivity().onBackPressed();

                break;
        }

    }


    private void openDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());


                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                tvTitleDate.setText(str_toDate);
                getHomeworkDateBy();


            }
        }, year, month, date);

        //  dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();

    }

    private void getHomeworkDateBy() {
        homeworkModelsInbox.clear();
       // EMPID = "cdfab2cd-9a3c-4e5e-9810-258f693bde93";
        apiService.get_teacher_homework_list(Constant.SCHOOL_ID, EMPID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            try {
                                Log.i("homeinboxSChool**", "" + response.body() + "*****" + response.code());

                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONArray jsonArray = object.getJSONArray("datadict");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject objectData = jsonArray.getJSONObject(i);


                                        if (objectData.isNull("homework_uuid")) {
                                            homeworkId = "";
                                        } else {
                                            homeworkId = objectData.getString("homework_uuid");
                                        }

                                        if (objectData.isNull("homework_title")) {
                                            homeworkTitle = "";
                                        } else {
                                            homeworkTitle = objectData.getString("homework_title");
                                        }
                                        if (objectData.isNull("description")) {
                                            description = "";
                                        } else {
                                            description = objectData.getString("description");
                                        }


                                        if (objectData.isNull("class")) {
                                            homeClass = "";
                                        } else {
                                            homeClass = objectData.getString("class");
                                        }
                                        if (objectData.isNull("section")) {
                                            section = "";
                                        } else {
                                            section = objectData.getString("section");
                                        }
                                        if (objectData.isNull("subject")) {
                                            subject = "";
                                        } else {
                                            subject = objectData.getString("subject");
                                        }
                                        if (objectData.isNull("start_date")) {
                                            startDate = "";
                                        } else {
                                            startDate = objectData.getString("start_date");
                                        }
                                        if (objectData.isNull("end_date")) {
                                            endDate = "";
                                        } else {
                                            endDate = objectData.getString("end_date");
                                        }

                                        if (tvTitleDate.getText().toString().equals(startDate)) {
                                            homeworkModelsInbox.add(new HomeworkModel(homeworkId, homeworkTitle, description,
                                                    homeClass, section, subject, startDate, endDate));

                                            Log.i("homeworkId**", "" + homeworkId + homeworkTitle);
                                        }

                                    }


                                    setRecyclerView(homeworkModelsInbox);
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

}
