package in.varadhismartek.patashalaerp.StudentModule;

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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.TeacherModule.TeacherHomeworkAdapter;
import in.varadhismartek.patashalaerp.TeacherModule.TeacherHomeworkModle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeWork_Fragment extends Fragment implements View.OnClickListener {
    ArrayList<TeacherHomeworkModle> teacherHomeworkModles;
    RecyclerView recycler_view;

    APIService apiService;
    String Student_ID = "", Student_Class = "", Student_Section = "", completePercentage = "", note = "", reportDate = "",
            assignerID = "", assigerFName = "", assignDate = "", assignerLName = "", startDate = "", endDate = "",
            studentFName = "", studentLName = "", StudentId = "", homeTitle = "", homeDescription = "",
            homeClass = "", homeSubject = "", homeSection = "", homeworkID = "", DOB = "", birthCertificate = "", currentDate = "",
            str_toDate = "", endYear = "";
    TeacherHomeworkAdapter teacherHomeworkAdapter;
    TextView tvTitle, tvTitle2, noRecords;
    private int year, month, date;

    Calendar calendar;
    Date date1;
    ImageView iv_backBtn;

    public StudentHomeWork_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_homework, container, false);

        initView(view);
        getHomework();
        setRecyclerView();
        return view;


    }

    private void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Student_ID = bundle.getString("STUDENTID");
            Student_Class = bundle.getString("STUDENT_CLASS");
            Student_Section = bundle.getString("STUDENT_SECTION");
        }

        Log.i("Student_ID**", "" + Student_ID + "**" + Student_Class + "**" + Student_Section);
        apiService = ApiUtils.getAPIService();
        recycler_view = view.findViewById(R.id.recycler_view);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        noRecords = view.findViewById(R.id.noRecords);
        teacherHomeworkModles = new ArrayList<>();

        tvTitle.setText("Homework List");

        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime());

        tvTitle2.setText(currentDate);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        tvTitle2.setOnClickListener(this);


    }


    private void getHomework() {
        /*  Student_Class = "ukg";
          Student_Section = "A";
         Student_ID = "938639c6-9d0f-4ec0-9732-1eba4f89fc2a";
          String scID = "55e9cd8c-052a-46b1-b81c-14f85e11a8fe";*/
        Log.i("Student_ID**5", "" + Student_ID + "**" + Student_Class + "**" + Student_Section);

        apiService.getHomeworkStudent(Constant.SCHOOL_ID, Student_Class, Student_Section, Student_ID)

                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Log.i("Re****", "" + response.body());


                            try {
                                teacherHomeworkModles.clear();
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
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");

                                        assignerID = objectData.getString("assigner_id");
                                        homeworkID = objectData.getString("homework_uuid");
                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                        assignDate = objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        DOB = objectData.getString("student_dob");
                                        birthCertificate = objectData.getString("student_birth_certificate_number");

                                        teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                completePercentage, note, reportDate, assignerID,
                                                assigerFName, assignerLName, startDate, endDate,
                                                studentFName, studentLName, StudentId, homeTitle,
                                                homeDescription, homeClass, homeSubject, homeSection,
                                                homeworkID, DOB, birthCertificate, assignDate
                                        ));
                                    }

                                    setRecyclerView();
                                }
                            } catch (JSONException je) {

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("Res[ponse ERR**", "" + t.toString());
                    }
                });
    }

    private void setRecyclerView() {
        if (teacherHomeworkModles.size() > 0) {
            noRecords.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
            teacherHomeworkAdapter = new TeacherHomeworkAdapter(teacherHomeworkModles, getActivity(),
                    Constant.RV_STUDENT_HOMEWORK_ROW);
            recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler_view.setAdapter(teacherHomeworkAdapter);
            teacherHomeworkAdapter.notifyDataSetChanged();
        } else {
            noRecords.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

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

                tvTitle2.setText(str_toDate);
                getHomeworkDateBy();


            }
        }, year, month, date);

        dialog.show();
    }

    private void getHomeworkDateBy() {

        apiService.getHomeworkStudent(Constant.SCHOOL_ID, Student_Class, Student_Section, Student_ID)

                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Log.i("Re****", "" + response.body());


                            try {
                                teacherHomeworkModles.clear();
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
                                        completePercentage = objectData.getString("completion_percent");

                                        note = objectData.getString("student_note");
                                        reportDate = objectData.getString("report_datetime");

                                        assignerID = objectData.getString("assigner_id");
                                        homeworkID = objectData.getString("homework_uuid");
                                        assigerFName = objectData.getString("assigner_first_name");
                                        assignerLName = objectData.getString("assigner_last_name");
                                        assignDate = objectData.getString("assigned_date");

                                        startDate = objectData.getString("start_date");
                                        endDate = objectData.getString("end_date");
                                        studentFName = objectData.getString("student_first_name");
                                        studentLName = objectData.getString("student_last_name");
                                        StudentId = objectData.getString("student_id");
                                        homeTitle = objectData.getString("homework_title");
                                        homeDescription = objectData.getString("description");
                                        homeClass = objectData.getString("class");
                                        homeSubject = objectData.getString("subject");
                                        homeSection = objectData.getString("section");

                                        DOB = objectData.getString("student_dob");
                                        birthCertificate = objectData.getString("student_birth_certificate_number");

                                        if (tvTitle2.getText().toString().contains(startDate))
                                            teacherHomeworkModles.add(new TeacherHomeworkModle(
                                                    completePercentage, note, reportDate, assignerID,
                                                    assigerFName, assignerLName, startDate, endDate,
                                                    studentFName, studentLName, StudentId, homeTitle,
                                                    homeDescription, homeClass, homeSubject, homeSection,
                                                    homeworkID, DOB, birthCertificate, assignDate
                                            ));
                                    }

                                    setRecyclerView();
                                }
                            } catch (JSONException je) {

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("Res[ponse ERR**", "" + t.toString());
                    }
                });
    }
}
