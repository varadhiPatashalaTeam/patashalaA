package in.varadhismartek.patashalaerp.AdmissionBarriers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.ValidationViews;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdmissionBarriersActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerCustomForFather, spinnerCustomForMother;
    EditText admission_number, percentage_for_admission, staffNumber;
    ToggleButton food_facility, transport_facility;
    TextView GuardiansNumber ;
    TextView increment, decrement;
    ImageView submit, backButton;
    Button btnSave;
    int minteger = 1;
    String qualification_father, qualification_mother, no_of_guardian;
    boolean foodRequired = false,b=false;
    boolean transportaion_required = false;
    int counter;
    String numberOfGuardians = "0", min_percentage = "0", transport_facilitys = "0", food_facilitys = "0",
            default_admisn_no = "0", father_qualification = "0", mother_qualification = "0";
    String regex = "([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*";
    private APIService mApiService;
    SharedPreferences preference;
    private double min = 35;
    private double max = 100;
    View v;
    String API_STATUS = "";
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barrier_admisssion);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        preference = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        Constant.SCHOOL_ID = preference.getString("SchoolId", "0");
        System.out.println("b*****11"+Constant.SCHOOL_ID);
        initViews();
        initListners();

        getStaffBarriers();
        getStudentBarriers();
    }


    private void initListners() {

        decrement.setEnabled(true);
        decrement.setOnClickListener(this);
        increment.setOnClickListener(this);
        submit.setOnClickListener(this);
        food_facility.setOnClickListener(this);
        transport_facility.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void initViews() {
        linearLayout = findViewById(R.id.linear2);
        spinnerCustomForFather = findViewById(R.id.spinnerCustomForFather);
        spinnerCustomForMother = findViewById(R.id.spinnerCustomForMother);
        // spinnerCustomForStudent = v.findViewById(R.id.spinnerCustomForStudentQualification);
        admission_number = findViewById(R.id.editText_admissionNumber);
        staffNumber = findViewById(R.id.editText_staffnumber);
        percentage_for_admission = findViewById(R.id.editText_percentage_for_admission);
        food_facility = findViewById(R.id.toggle_food_facility);
        transport_facility = findViewById(R.id.toggle_transport_facility);
        GuardiansNumber = findViewById(R.id.numberOfGuardians);
        increment = findViewById(R.id.button_increase_count);
        decrement = findViewById(R.id.button_decrease_count);
        submit = findViewById(R.id.btnNext);
        backButton = findViewById(R.id.iv_backBtn);
        btnSave = findViewById(R.id.btnSave);
        linearLayout.setVisibility(View.GONE);
        mApiService = ApiUtils.getAPIService();


        SharedPreferences sp = this.getSharedPreferences("values", 0);
        counter = sp.getInt("counter", 0);
        if (counter == 0) {
            SharedPreferences.Editor et = sp.edit();
            et.putInt("counter", 1);
            et.apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:

                if (b) {
                    Intent intent = new Intent(AdmissionBarriersActivity.this, DashboardActivity.class);

                    startActivity(intent);
                }else {
                    Toast.makeText(AdmissionBarriersActivity.this,"First Save Barriers",Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.button_decrease_count:

                if (minteger == 1) {
                    decrement.setEnabled(false);

                } else {
                    minteger = minteger - 1;
                    increment.setEnabled(true);
                    displayNumberOfGuardian(minteger);
                }


                break;

            case R.id.button_increase_count:
                minteger = minteger + 1;


                decrement.setEnabled(true);
                displayNumberOfGuardian(minteger);
                if (minteger == 3) {
                    increment.setEnabled(false);
                }
                break;


            case R.id.toggle_food_facility:
                if (food_facility.isChecked()) {
                    foodRequired = true;
                    Log.d("boolean", "food=true");
                }

                break;

            case R.id.toggle_transport_facility:
                if (transport_facility.isChecked()) {
                    transportaion_required = true;
                    Log.d("boolean", "transport=true");
                }


                break;

            case R.id.btnSave:


                if (!ValidationViews.EditTextNullValidate(staffNumber, admission_number/*, percentage_for_admission*/)) {
                    ValidationViews.showToast(this, "Please fill detail");
                    return;
                }
                if (!staffNumber.getText().toString().trim().matches(regex)) {
                    staffNumber.setError("ABC000");
                    return;
                }
                if (!admission_number.getText().toString().trim().matches(regex)) {
                    admission_number.setError("ABC000");
                    return;
                }
                else {
                    //updateStaffBarriers();
                    getStaffBarriers();

                }


                break;
            case R.id.iv_backBtn:
                this.finish();
                //  onBackPressed();


                break;
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void getStaffBarriers() {
        mApiService.getStaffBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("STAFF_GET12", "" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String message = json1.get("message").toString();
                        JSONObject objectData = json1.getJSONObject("data");
                        String staffID =objectData.getString("default_teacher_admission_no");


                        if (message.equalsIgnoreCase("School staff barrier details")) {
                            staffNumber.setText(staffID);
                            updateStaffBarriers();
                        } else if (message.equalsIgnoreCase("School staff barrier details not exists")) {
                            addStaffBarriers();
                        } else {
                            Toast.makeText(AdmissionBarriersActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    if ((response.message()).equalsIgnoreCase("School staff barrier details not exists")){
                        addStaffBarriers();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void addStaffBarriers() {

        mApiService.addStaffBarrier(Constant.SCHOOL_ID, staffNumber.getText().toString(), Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("STAFF_BARRIERS_add", "" + response.body());
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String Status = json1.get("status").toString();
                                if (Status.equals("Success")) {
                                    Log.i("ADD STAFF", "" + response.body());
                                    getStudentBarriers();
                                }
                            } catch (JSONException je) {

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("STAFF_BARRIERS__ERROR", "" + t.toString());
                    }
                });
    }

    private void getStudentBarriers() {

        mApiService.getstatusStudentBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject data = json1.getJSONObject("data");
                            Log.i("Stu_barriers**", ""+data);
                            String studentID = data.getString("default_student_admission_no");
                            admission_number.setText(studentID);

                            Log.d("minGua", "" + min_percentage + transport_facilitys + food_facilitys +
                                    no_of_guardian + father_qualification + mother_qualification + default_admisn_no);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("ERROR**", t.toString());

            }
        });


    }

    private void updateStaffBarriers() {
        mApiService.updateStaffBarriers(Constant.SCHOOL_ID, staffNumber.getText().toString(), Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("STAFF_BARRIERS_Update", "" + response.body());
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String Status = json1.get("status").toString();
                                if (Status.equals("Success")) {
                                    Log.i("UPDATE STAFF", "" + response.body());
                                  addStudentBarriers();

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



    private void addStudentBarriers() {

        mApiService.addStudentBarrier(Constant.SCHOOL_ID, admission_number.getText().toString(), min_percentage,
                father_qualification, mother_qualification, food_facilitys, transport_facilitys,
                numberOfGuardians, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    b=true;
                    Log.i("STUDENT_BARRIERS_ADD1", "" + response.body());
                    try {
                        JSONObject jsonObject = (new JSONObject(new Gson().toJson(response.body())));
                        String Status = jsonObject.getString("status");
                        if (Status.equalsIgnoreCase("Success")) {

                            Toast.makeText(AdmissionBarriersActivity.this,
                                    "Added Successfully", Toast.LENGTH_SHORT).show();
                            admission_number.setText("");
                            staffNumber.setText("");
                            percentage_for_admission.setText("");
                            spinnerCustomForFather.setSelection(0);
                            spinnerCustomForMother.setSelection(0);
                            GuardiansNumber.setText("");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Log.i("STUDENT_BARRIERS_ERROR", "" + t.toString());
            }
        });
    }


    private void displayNumberOfGuardian(int minteger) {
        GuardiansNumber.setText("" + minteger);
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

}