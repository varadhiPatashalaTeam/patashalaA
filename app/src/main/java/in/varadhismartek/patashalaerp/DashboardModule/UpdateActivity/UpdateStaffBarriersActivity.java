package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.ValidationViews;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStaffBarriersActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivNext;
    Button buttonSave;
    ImageView backButton;
    EditText admission_number, staffNumber, percentage_for_admission;
    ToggleButton food_facility, transport_facility;
    TextView GuardiansNumber, nextTimeTextView, getadmissionValue;
    Spinner spinnerCustomForFather, spinnerCustomForMother, spinnerCustomForStudent;
    TextView increment, decrement;
    SharedPreferences preference;
    APIService mApiService;
    String regex = "([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*";
    String min_percentage = "0", facilityTransport = "0", facilityFood = "0", qual_father = "0", qual_mother = "0", guardiansNo = "0";
    int minteger = 1;
    boolean foodRequired = false;
    boolean transportaion_required = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barrier_admisssion);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        preference = getSharedPreferences("SecurityStatus", Context.MODE_PRIVATE);
        Constant.SCHOOL_ID = preference.getString("SchoolId", "0");

        mApiService = ApiUtils.getAPIService();
        initViews();
        initListners();

        getStaffBarriers();

        getStudentBarriers();

        initSpinnerForFather();
        initSpinnerForMother();

    }

    private void initViews() {
        ivNext = findViewById(R.id.btnNext);
        admission_number = findViewById(R.id.editText_admissionNumber);
        staffNumber = findViewById(R.id.editText_staffnumber);
        percentage_for_admission = findViewById(R.id.editText_percentage_for_admission);
        food_facility = findViewById(R.id.toggle_food_facility);
        transport_facility = findViewById(R.id.toggle_transport_facility);
        GuardiansNumber = findViewById(R.id.numberOfGuardians);
        spinnerCustomForFather = findViewById(R.id.spinnerCustomForFather);
        spinnerCustomForMother = findViewById(R.id.spinnerCustomForMother);
        backButton = findViewById(R.id.iv_backBtn);
        buttonSave = findViewById(R.id.btnSave);
        increment = findViewById(R.id.button_increase_count);
        decrement = findViewById(R.id.button_decrease_count);
        ivNext.setVisibility(View.GONE);
    }

    private void initListners() {
        decrement.setEnabled(true);
        decrement.setOnClickListener(this);
        increment.setOnClickListener(this);
        backButton.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        food_facility.setOnClickListener(this);
        transport_facility.setOnClickListener(this);

    }

    private void initSpinnerForMother() {
        ArrayList<String> qualificationArrayList = new ArrayList<String>();
        qualificationArrayList.add("Select");
        qualificationArrayList.add("Not mandatory");
        qualificationArrayList.add("Under Graduate");
        qualificationArrayList.add("Graduate");
        qualificationArrayList.add("Post Graduate");
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, qualificationArrayList,"");
        spinnerCustomForMother.setAdapter(customSpinnerAdapter);
        spinnerCustomForMother.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                qual_mother = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinnerForFather() {
        ArrayList<String> qualificationArrayList = new ArrayList<String>();
        qualificationArrayList.add("Select");
        qualificationArrayList.add("Not mandatory");
        qualificationArrayList.add("Under Graduate");
        qualificationArrayList.add("Graduate");
        qualificationArrayList.add("Post Graduate");

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, qualificationArrayList,"");
        spinnerCustomForFather.setAdapter(customSpinnerAdapter);
        spinnerCustomForFather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                qual_father = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getStaffBarriers() {
        mApiService.getStaffBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("STAFF_GET", "" + response.body());
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String message = json1.get("message").toString();
                        JSONObject objectData = json1.getJSONObject("data");
                        String staffID = objectData.getString("default_teacher_admission_no");

                        if (message.equalsIgnoreCase("School staff barrier details")) {
                            staffNumber.setText(staffID);
                            //updateStaffBarriers();
                        } else if (message.equalsIgnoreCase("School staff barrier details not exists")) {
                            // addStaffBarriers();
                        } else {
                            Toast.makeText(UpdateStaffBarriersActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getStudentBarriers() {

        mApiService.getstatusStudentBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("Stu_barriers**", "" + response.body());

                    try {

                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String message = (String) json1.get("message");

                        if (message.equalsIgnoreCase("School student barrier details")) {

                            JSONObject data = json1.getJSONObject("data");
                            Log.i("Stu_barriers**", "" + data);
                            String studentID = data.getString("default_student_admission_no");


                            min_percentage = data.get("minimum_percentage_required").toString();
                            facilityTransport = data.get("transport_facility").toString();
                            facilityFood = data.get("food_facility").toString();
                            qual_father = data.get("father_qualification").toString();
                            qual_mother = data.get("mother_qualification").toString();
                            guardiansNo = data.get("no_of_guardians").toString();

                            admission_number.setText(studentID);
                            percentage_for_admission.setText(min_percentage);
                            spinnerCustomForFather.setSelection(getIndex(spinnerCustomForFather, qual_father));
                            spinnerCustomForMother.setSelection(getIndex(spinnerCustomForMother, qual_mother));

                            if (facilityFood.equals("true"))
                                food_facility.setChecked(true);
                            else food_facility.setChecked(false);

                            if (facilityTransport.equals("true"))
                                transport_facility.setChecked(true);
                            else transport_facility.setChecked(false);

                            minteger = Integer.parseInt(String.valueOf(guardiansNo.charAt(0)));
                            displayNumberOfGuardian(minteger);

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
                                    updateStudentBarriers();

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

    private void updateStudentBarriers() {
        min_percentage = percentage_for_admission.getText().toString();
        guardiansNo = GuardiansNumber.getText().toString();
        facilityFood = String.valueOf(foodRequired);
        facilityTransport = String.valueOf(transportaion_required);


        mApiService.addStudentBarrier(Constant.SCHOOL_ID, admission_number.getText().toString(), min_percentage,
                qual_father, qual_mother, facilityFood, facilityTransport,
                guardiansNo, Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("STUDENT_BARRIERS_ADD1", "" + response.body());
                    try {
                        JSONObject jsonObject = (new JSONObject(new Gson().toJson(response.body())));
                        String Status = jsonObject.getString("status");
                        if (Status.equalsIgnoreCase("Success")) {

                            Toast.makeText(UpdateStaffBarriersActivity.this,
                                    "Barrier have save successfully", Toast.LENGTH_SHORT).show();

                            admission_number.setText("");
                            staffNumber.setText("");
                            percentage_for_admission.setText("");
                            spinnerCustomForFather.setSelection(0);
                            spinnerCustomForMother.setSelection(0);
                            GuardiansNumber.setText("");

                            finish();
                            Intent intent = new Intent(UpdateStaffBarriersActivity.this, UpdateStaffBarriersActivity.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(UpdateStaffBarriersActivity.this,
                                    "Invalid Staff admission no ", Toast.LENGTH_SHORT).show();
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


    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void displayNumberOfGuardian(int minteger) {
        GuardiansNumber.setText("" + minteger);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:

                Intent intent = new Intent(UpdateStaffBarriersActivity.this, DashboardSettingActivity.class);
                startActivity(intent);
                finish();
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
                } else {
                    foodRequired = false;
                }

                break;

            case R.id.toggle_transport_facility:
                if (transport_facility.isChecked()) {
                    transportaion_required = true;
                    Log.d("boolean", "transport=true");
                } else {
                    transportaion_required = false;
                }
                break;


            case R.id.btnSave:
                if (!ValidationViews.EditTextNullValidate(staffNumber, admission_number/*, percentage_for_admission*/)) {
                    ValidationViews.showToast(this, "Please fill detail");
                    return;
                }
                if (!staffNumber.getText().toString().trim().matches(regex)) {
                    staffNumber.setError("Enter the following format ABC000 ");
                    return;
                }
                if (!admission_number.getText().toString().trim().matches(regex)) {
                    admission_number.setError("Enter the following format ABC000");
                    return;
                } else {
                    //addStaffBarriers();
                    // updateStudentBarriers();
                    updateStaffBarriers();

                }
                break;
        }
    }
}
