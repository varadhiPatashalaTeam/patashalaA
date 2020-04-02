package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;


import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Locale;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DivisionModule.AddDivisionActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class AddFeeStructure extends AppCompatActivity implements View.OnClickListener {
    EditText edFeeType, edFeeCode, edInstallment;
    TextView edFeeDueDate;
    Spinner spn_academicyear;
    Button btnAdd;
    APIService apiService,mApiService;

    String  str_toDate="", startYear = "", endYear = "", sDate = "", eDate = "",strSelectSession="",SubfromDate="";
    ArrayList<String> spinnerList= new ArrayList<>();
    ArrayList<String>spinnerDateList= new ArrayList<>();

    private int year, month, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee_structure);

        apiService = ApiUtils.getAPIService();
        mApiService = ApiUtilsPatashala.getService();

        edFeeType = findViewById(R.id.et_fee_type);
        edFeeCode = findViewById(R.id.et_fee_code);
        edInstallment = findViewById(R.id.et_installment);
        edFeeDueDate = findViewById(R.id.tv_due_date);

        btnAdd = findViewById(R.id.bt_update);
        spn_academicyear = findViewById(R.id.spn_acadmicyear);
        edFeeDueDate.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        getAcadmicAPI();
    }

    private void getAcadmicAPI() {

        mApiService.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.d("SESSION**AYEAR", "onResponse: " + response.body());
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

    private void setSpinner() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, spinnerList);
        spn_academicyear.setAdapter(adapter);
        spn_academicyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strAcadmicYear = spn_academicyear.getItemAtPosition(position).toString();
                if (!strAcadmicYear.equalsIgnoreCase("Select Academic year")){
                    int pos = parent.getSelectedItemPosition();


                    strSelectSession = spinnerDateList.get(pos);
                    String str_sessionName = spn_academicyear.getSelectedItem().toString();
                    System.out.println("str_sessionName**1**" + spinnerDateList.get(pos)+"****"+str_sessionName);
                    //SubtoDate = strSelectSession.substring(13);

                    SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));
                    Constant.ACADEMIC_YEAR =SubfromDate;
                    Log.d(TAG, "onResponse:getsession " + SubfromDate + "***" );

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_due_date:
                dateDialog();
                break;
            case R.id.bt_update:
                Constant.FEE_LIST_SIZE++;
                JSONObject object = new JSONObject();
                JSONObject objectOreder = new JSONObject();
                int i = 0;

                try {
                    object.put("session_serial_no",  String.valueOf(Constant.FEE_LIST_SIZE++));
                    object.put("fee_type", edFeeType.getText().toString());
                    object.put("installment", edInstallment.getText().toString());
                    object.put("due_date", edFeeDueDate.getText().toString());
                    object.put("fee_code", edFeeCode.getText().toString());
                    object.put("status", "true");

                    objectOreder.put(String.valueOf(i + 1), object);
                } catch (JSONException je) {

                }
                createFeeMthod(objectOreder);
                System.out.println("objectOreder**" + objectOreder);

                break;
        }
    }
    private void dateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar;;
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                endYear = yearFormat.format(calendar.getTime());
                str_toDate = simpleDateFormat.format(calendar.getTime());
                Log.d("CHECK_DATE", str_toDate);
                Date date1 = new Date();
                try {
                    date1 = simpleDateFormat.parse(str_toDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edFeeDueDate.setText(str_toDate);
            }
        }, year, month, date);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void createFeeMthod(JSONObject objectOreder) {
        apiService.createFeeStructure(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID, SubfromDate,
                objectOreder).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("FEES RESP**44", "" + response.body() + "***" + response.code());
                if (response.isSuccessful()) {
                    Log.i("FEES RESP**44", "" + response.body() + "***" + response.code());
                    Toast.makeText(AddFeeStructure.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    edFeeType.setText("");
                    edFeeCode.setText("");
                    edInstallment.setText("");
                } else {

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("FEES RESP**445", "" + t.toString());
            }
        });
    }

}