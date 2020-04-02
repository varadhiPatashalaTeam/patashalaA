package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter.UpdateDivisionAdapter;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class UpdateSessionsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_backBtn, ivNext;
    private TextView tv_fromDate, tv_toDate;
    private RelativeLayout rl_fromDate, rl_toDate;
    private Button bt_add, bt_submit;
    private Spinner spinner;
    private TextView tvSessionName, tv_minus, tv_noSession, tv_plus;
    private RecyclerView recycler_view;
    ImageView iv_down, iv_up;
    LinearLayout ll_showHide, ll_NoOfSession;


    private int year, month, date, rowNumber = 1;
    private String currentDate, str_sessionName = "", strSelectSession = "", str_from_date, str_toDate,
            startYear = "", endYear = "", sDate = "", eDate = "";
    ArrayList<String> sessionList, spinnerList, spinnerDateList;
    ArrayList<Integer> list;
    CardView cardView;
    APIService apiService;
    List<ClassSectionAndDivisionModel> modelList = new ArrayList<>();

    String res_to_date, res_from_date;
    String SubtoDate = "", SubfromDate = "";
    boolean b = true;
    public static EditText edWorkDay;
    Date date1, date2;
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_session);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        apiService = ApiUtilsPatashala.getService();

        edWorkDay = findViewById(R.id.workday);
        iv_backBtn = findViewById(R.id.iv_backBtn);
        ivNext = findViewById(R.id.btnNext);
        tv_fromDate = findViewById(R.id.tv_fromDate);
        tv_toDate = findViewById(R.id.tv_toDate);
        spinner = findViewById(R.id.spinnerForSession);
        bt_add = findViewById(R.id.bt_add);
        bt_submit = findViewById(R.id.bt_submit);
        //recycler_view = findViewById(R.id.recycler_view);

        rl_fromDate = findViewById(R.id.rl_fromDate);
        rl_toDate = findViewById(R.id.rl_toDate);

        tvSessionName = findViewById(R.id.tvSessionName);
        tv_minus = findViewById(R.id.tv_minus);
        tv_noSession = findViewById(R.id.tv_noSession);
        tv_plus = findViewById(R.id.tv_plus);
        recycler_view = findViewById(R.id.recycler_view);

        iv_down = findViewById(R.id.iv_down);
        iv_up = findViewById(R.id.iv_up);
        ll_showHide = findViewById(R.id.ll_showHide);
        ll_NoOfSession = findViewById(R.id.ll_NoOfSession);
        ivNext.setVisibility(View.GONE);

        sessionList = new ArrayList<>();
        spinnerList = new ArrayList<>();
        spinnerDateList = new ArrayList<>();

        list = new ArrayList<>();
        modelList = new ArrayList<>();
        modelList.clear();
        list.add(0, 1);
        // modelList.add(0, 1);


        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = simpleDate.format(calendar.getTime());
        edWorkDay.setText("1");

        modelList.add(new ClassSectionAndDivisionModel(Constant.EMPLOYEE_BY_ID, Constant.POC_NAME,
                tv_noSession.getText().toString(), SubfromDate, SubtoDate, currentDate, currentDate));


        iv_backBtn.setOnClickListener(this);
        rl_fromDate.setOnClickListener(this);
        rl_toDate.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        getAcademicYear();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                Intent intent = new Intent(UpdateSessionsActivity.this, DashboardSettingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_fromDate:
                fromDatePickerDialog();
                break;
            case R.id.rl_toDate:
                toDatePickerDialog();
                break;

            case R.id.bt_add:

                if (tv_fromDate.getText().toString().equals("From Date") || tv_toDate.getText().toString().equals("To Date")) {
                    Toast.makeText(UpdateSessionsActivity.this, "Please Select From & To date", Toast.LENGTH_SHORT).show();
                } else if (date2.after(date1)) {
                    Toast.makeText(UpdateSessionsActivity.this, "To Date greater than From Date ", Toast.LENGTH_SHORT).show();
                } else {


                    String selectedDate = str_from_date + " - " + str_toDate;
                    String selectedYear = startYear + " - " + endYear;

                    spinnerList.add(selectedYear);
                    spinnerDateList.add(selectedDate);

                    setSpinner();
                    tv_fromDate.setText("From Date");
                    tv_toDate.setText("To Date");
                    Toast.makeText(UpdateSessionsActivity.this, "Date Added Successfully", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_plus:
                if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(UpdateSessionsActivity.this, "Select Academic Year", Toast.LENGTH_SHORT).show();
                } else {

                    if (rowNumber < 4) {
                        rowNumber = Integer.parseInt(tv_noSession.getText().toString());
                        rowNumber++;
                        tv_noSession.setText(String.valueOf(rowNumber));
                        Log.d("BUTTON****4+-", String.valueOf(rowNumber));
                        Gson gson = new Gson();
                        String fromSessionDate = "", newFromSessDate1 = "", newFromSessDate2 = "",
                                lastSessionDate = "", SesStartDate = "", SesEndstDate = "";


                        fromSessionDate = String.valueOf(modelList.get(modelList.size() - 1).getAcd_from_date());

                        lastSessionDate = String.valueOf(modelList.get(modelList.size() - 1).getAcd_to_date());

                        SesStartDate = String.valueOf(modelList.get(modelList.size() - 1).getRespStartDate());

                        SesEndstDate = String.valueOf(modelList.get(modelList.size() - 1).getRespEndDate());

                        Log.d("BUTTON****date", "" + lastSessionDate + "**" + SesStartDate + "**" + SesEndstDate);


                        //if (b) {
                        //   b = false;
                        modelList.remove(modelList.size() - 1);
                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(sdf.parse(SesStartDate));
                            c.add(Calendar.DATE, 1);
                            newFromSessDate1 = sdf.format(c.getTime());  // dt is now the new date
                            Log.d("ChangeDate***1**", "" + newFromSessDate1);

                            modelList.add(new ClassSectionAndDivisionModel(Constant.EMPLOYEE_BY_ID, Constant.POC_NAME,
                                    tv_noSession.getText().toString(), SubfromDate, SubtoDate, SesStartDate, newFromSessDate1));

                            c.setTime(sdf.parse(newFromSessDate1));
                            c.add(Calendar.DATE, 1);
                            newFromSessDate2 = sdf.format(c.getTime());
                            Log.d("ChangeDate***12**", "" + newFromSessDate2);

                            modelList.add(new ClassSectionAndDivisionModel(Constant.EMPLOYEE_BY_ID, Constant.POC_NAME,
                                    tv_noSession.getText().toString(), SubfromDate, SubtoDate, newFromSessDate2, SesEndstDate));


                        } catch (ParseException pe) {

                        }


                        UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(modelList, UpdateSessionsActivity.this, Constant.RV_SESSION_ROW);
                        recycler_view.setLayoutManager(new LinearLayoutManager(UpdateSessionsActivity.this));
                        recycler_view.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    }
                }
                break;
            case R.id.tv_minus:
                rowNumber = Integer.parseInt(tv_noSession.getText().toString());
                if (rowNumber > 1) {

                    Log.d("BUTTON****2-A", String.valueOf(rowNumber) + "***" + modelList.size());

                    String mLastRowStartDate = "", mLastRowEndDate = "", mSLastRowStartDate = "", mSLastRowEndDate = "";
                    mLastRowEndDate = modelList.get(modelList.size() - 1).getRespEndDate();
                    mSLastRowStartDate = modelList.get(modelList.size() - 2).getRespStartDate();
                    mSLastRowEndDate = modelList.get(modelList.size() - 2).getRespEndDate();


                    rowNumber--;
                    tv_noSession.setText(String.valueOf(rowNumber));
                    modelList.remove(modelList.size() - 1);
                    modelList.remove(modelList.size() - 1);
                    modelList.add(new ClassSectionAndDivisionModel(Constant.EMPLOYEE_BY_ID, Constant.POC_NAME, tv_noSession.getText().toString(),
                            SubfromDate, SubtoDate, mSLastRowStartDate, mLastRowEndDate));


                    UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(modelList, UpdateSessionsActivity.this, Constant.RV_SESSION_ROW);
                    recycler_view.setLayoutManager(new LinearLayoutManager(UpdateSessionsActivity.this));
                    recycler_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
                break;
            case R.id.bt_submit:

                SubtoDate = strSelectSession.substring(13);
                SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    date1 = formatter.parse(SubfromDate);
                    date2 = formatter.parse(SubtoDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (date1.compareTo(date2) > 0) {
                    Toast.makeText(UpdateSessionsActivity.this, "To Date greater than From Date ", Toast.LENGTH_SHORT).show();
                } else if (edWorkDay.getText().toString().isEmpty() || edWorkDay.getText().toString().length() == 0) {
                    Toast.makeText(UpdateSessionsActivity.this, "Please enter working day", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject sessionObject = new JSONObject();
                    JSONObject mainObject = new JSONObject();
                    Gson gson = new Gson();

                    for (int i = 0; i < modelList.size(); i++) {
                        String strFromDate = modelList.get(i).getRespStartDate();
                        String strToDate = modelList.get(i).getRespEndDate();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("from_date", strFromDate);
                            jsonObject.put("to_date", strToDate);
                            mainObject.put(String.valueOf(i + 1), jsonObject);
                            sessionObject.put("session", mainObject);

                        } catch (JSONException je) {

                        }

                    }

                    InputMethodManager imm = (InputMethodManager) UpdateSessionsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    addSessionAPI(sessionObject);

                }

                break;
        }
    }

    private void addSessionAPI(JSONObject sessionObject) {
        apiService.addSession(Constant.SCHOOL_ID, str_from_date, str_toDate, edWorkDay.getText().toString(),
                sessionObject, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("SESSION_RESS", "" + response.body());
                Log.i("SESSION_RESS", "" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateSessionsActivity.this, "Session have save successfully ", Toast.LENGTH_SHORT).show();
                    spinner.setSelection(0);
                    tv_fromDate.setText("");
                    tv_toDate.setText("");
                    edWorkDay.setText("");
                    getAcademicYear();


                } else {
                    if ((String.valueOf(response.code()).equals("400")) || (String.valueOf(response.code()).equals("400"))) {
                        Toast.makeText(UpdateSessionsActivity.this, "Session already exist ", Toast.LENGTH_SHORT).show();
                        spinner.setSelection(0);
                        tv_fromDate.setText("");
                        tv_toDate.setText("");
                        edWorkDay.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
    private void setSpinner() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(UpdateSessionsActivity.this, spinnerList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner.getSelectedItemPosition() != 0) {
                    b = true;
                    rowNumber = 1;
                    tv_noSession.setText(String.valueOf(rowNumber));
                    list.clear();
                    edWorkDay.setText("");
                    modelList.clear();
                    list.add(0, 1);


                    int pos = adapterView.getSelectedItemPosition();
                    System.out.println("str_sessionName**1**" + spinnerDateList.get(pos));


                    strSelectSession = spinnerDateList.get(pos);
                    str_sessionName = spinner.getSelectedItem().toString();
                    tvSessionName.setText(str_sessionName);

                    getSchoolSession(strSelectSession);


                } else {
                    UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(modelList, UpdateSessionsActivity.this, Constant.RV_SESSION_ROW);
                    recycler_view.setLayoutManager(new LinearLayoutManager(UpdateSessionsActivity.this));
                    recycler_view.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAcademicYear() {
        spinnerList.clear();
        spinnerDateList.clear();

        spinnerList.add(0, "Select Academic Year");
        spinnerDateList.add(0, "Select Academic Year");
        apiService.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("SESSION**AYEAR", "onResponse: " + response.body());
                Log.d("SESSION**AYEAR", "onResponse: " + response.code()+ Constant.SCHOOL_ID);

                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = object.getJSONObject("data");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("Academic_years");

                            Iterator<String> keys = jsonObject2.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);
                                String resStartDate = jsonObjectValue.getString("start_date");
                                String resEndDate = jsonObjectValue.getString("end_date");


                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");

                                try {
                                    Date fromYear = formater.parse(resStartDate);
                                    Date toYear = formater.parse(resEndDate);

                                    sDate = formatterDate.format(fromYear);
                                    eDate = formatterDate.format(toYear);

                                    startYear = formatterYear.format(fromYear);
                                    endYear = formatterYear.format(toYear);

                                    String selectedDate = sDate + " - " + eDate;
                                    String selectedYear = startYear + " - " + endYear;

                                    spinnerList.add(selectedYear);
                                    spinnerDateList.add(selectedDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                            setSpinner();


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

    private void getSchoolSession(String strSelectSession) {

        SubtoDate = strSelectSession.substring(13);
        SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));
        Log.d(TAG, "onResponse:getsession " + SubfromDate + "***" + SubtoDate);

        apiService.getSessions(Constant.SCHOOL_ID, SubfromDate, SubtoDate)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d(TAG, "onResponse:getsession " + response.body());
                        Gson gson = new Gson();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                String status = (String) object.get("status");

                                if (status.equalsIgnoreCase("success")) {
                                    JSONObject jsonObject1 = object.getJSONObject("data");
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("sessions");

                                    Iterator<String> keys = jsonObject2.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject jsonObjectValue = jsonObject2.getJSONObject(key);


                                        String added_employeeid = jsonObjectValue.getString("added_employeeid");
                                        String added_employee_name = jsonObjectValue.getString("added_employee_name");
                                        res_to_date = jsonObjectValue.getString("to_date");
                                        res_from_date = jsonObjectValue.getString("from_date");
                                        String session_serial_no = jsonObjectValue.getString("session_serial_no");

                                        edWorkDay.setText(session_serial_no);
                                        modelList.add(new ClassSectionAndDivisionModel(added_employeeid, added_employee_name,
                                                session_serial_no, SubfromDate, SubtoDate, res_from_date, res_to_date));

                                    }

                                    tv_noSession.setText(String.valueOf(modelList.size()));
                                    UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(modelList, UpdateSessionsActivity.this, Constant.RV_SESSION_ROW);
                                    recycler_view.setLayoutManager(new LinearLayoutManager(UpdateSessionsActivity.this));
                                    recycler_view.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException je) {

                            }
                        } else {
                            if ((String.valueOf(response.code()).equals("404"))) {
                                tv_noSession.setText("1");

                                modelList.add(new ClassSectionAndDivisionModel(Constant.EMPLOYEE_BY_ID, Constant.POC_NAME,
                                        tv_noSession.getText().toString(), SubfromDate, SubtoDate, str_from_date, str_toDate));

                                UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(modelList, UpdateSessionsActivity.this, Constant.RV_SESSION_ROW);
                                recycler_view.setLayoutManager(new LinearLayoutManager(UpdateSessionsActivity.this));
                                recycler_view.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }
    private void toDatePickerDialog() {


        DatePickerDialog dialog = new DatePickerDialog(UpdateSessionsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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

                tv_toDate.setText(str_toDate);
            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();


    }

    private void fromDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(UpdateSessionsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                startYear = yearFormat.format(calendar.getTime());
                str_from_date = simpleDateFormat.format(calendar.getTime());

                Log.d("CHECK_DATE", str_from_date);

                tv_fromDate.setText(str_from_date);
                date2 = new Date();
                try {
                    date2 = simpleDateFormat.parse(str_from_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

}
