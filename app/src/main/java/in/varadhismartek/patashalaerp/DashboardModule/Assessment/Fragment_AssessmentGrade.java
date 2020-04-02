package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class Fragment_AssessmentGrade extends Fragment implements View.OnClickListener {

    ImageView iv_backBtn;
    TextView tv_App, tv_Ap, tv_A, tv_B_plus, tv_B, tv_C, tv_D;
    RecyclerView recycler_view;

    String str_grade = "";
    ArrayList<AssesmentModel> arrayList;
    long str_minMarks, str_maxMarks;
    APIService mApiService, apiService;

    String str_toDate = "", startYear = "", endYear = "", sDate = "", eDate = "", strSelectSession = "", SubfromDate = "";
    ArrayList<String> sessionList, spinnerList, spinnerDateList;
    Spinner spn_AcdamicYear;
    Button btnSave,btnUpdate;
    int saveButtonClick =1;

    public Fragment_AssessmentGrade() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);

        initViews(view);
        initListeners();
        getGradeBarriers();
        getAcadmicAPI();
        //setRecyclerViw();

        return view;
    }

    private void initViews(View view) {
        mApiService = ApiUtils.getAPIService();
        apiService = ApiUtilsPatashala.getService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        tv_App = view.findViewById(R.id.tv_App);
        tv_Ap = view.findViewById(R.id.tv_Ap);
        tv_A = view.findViewById(R.id.tv_A);

        tv_B_plus = view.findViewById(R.id.tv_B_plus);
        tv_B = view.findViewById(R.id.tv_B);

        tv_C = view.findViewById(R.id.tv_C);
        tv_D = view.findViewById(R.id.tv_D);
        spn_AcdamicYear = view.findViewById(R.id.spn_acadmic_year);
        recycler_view = view.findViewById(R.id.recycler_view);

        btnSave = view.findViewById(R.id.button_added);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        arrayList = new ArrayList<>();
        spinnerList = new ArrayList<>();
        spinnerDateList = new ArrayList<>();
        spinnerList.clear();
        spinnerDateList.clear();
        spinnerList.add("Select Academic Year");
    }

    private void initListeners() {

        iv_backBtn.setOnClickListener(this);
        tv_App.setOnClickListener(this);
        tv_Ap.setOnClickListener(this);
        tv_A.setOnClickListener(this);
        tv_B_plus.setOnClickListener(this);
        tv_B.setOnClickListener(this);
        tv_C.setOnClickListener(this);
        tv_D.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void getAcadmicAPI() {

        apiService.getAcademicYear(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        Log.d("SESSION**AYEAR", "onResponse: "+ response.code() + response.body());

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
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), spinnerList);
        spn_AcdamicYear.setAdapter(adapter);
        spn_AcdamicYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strAcadmicYear = spn_AcdamicYear.getItemAtPosition(position).toString();
                if (!strAcadmicYear.equalsIgnoreCase("Select Academic Year")) {
                    int pos = parent.getSelectedItemPosition();
                    strSelectSession = spinnerDateList.get(pos-1);
                    String str_sessionName = spn_AcdamicYear.getSelectedItem().toString();
                    System.out.println("str_sessionName**1**" + spinnerDateList.get(pos-1) + "****" + str_sessionName);
                    //SubtoDate = strSelectSession.substring(13);
                    SubfromDate = strSelectSession.substring(0, Math.min(strSelectSession.length(), 10));
                    Log.d(TAG, "onResponse:getsession " + SubfromDate + "***");
                    getGradeBarriers();
                } else {
                    Toast.makeText(getActivity(), "Select Academic Year", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getGradeBarriers() {
        arrayList.clear();
        System.out.println("SubfromDate******222****"+SubfromDate);
        mApiService.getGradeBarrier(Constant.SCHOOL_ID,SubfromDate).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("ADDGrade_Response1", "" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            arrayList.clear();
                            Log.i("ADDGrade_Response", "" + response.body());

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                Log.d("jsonSliderValue", jsonObjectValue.toString());

                                boolean gradeStatus = jsonObjectValue.getBoolean("status");

                                if (gradeStatus) {
                                    String gradeName = jsonObjectValue.getString("grade");
                                    long gradeMixMarks = jsonObjectValue.getLong("from_mark");
                                    long gradeMAxMArks = jsonObjectValue.getLong("to_mark");

                                    arrayList.add(new AssesmentModel(gradeName, gradeMixMarks, gradeMAxMArks));
                                }

                            }
                            if (arrayList.size()>0){
                                btnUpdate.setVisibility(View.VISIBLE);
                                btnSave.setVisibility(View.GONE);
                                setRecyclerViw();
                            }
                            else {
                                btnSave.setVisibility(View.VISIBLE);
                                btnUpdate.setVisibility(View.GONE);
                            }


                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.d("DEPT_FAIL", String.valueOf(response.code()));
                    if (String.valueOf(response.code()).equals("409")) {
                        Toast.makeText(getActivity(), "No Records", Toast.LENGTH_SHORT).show();


                    }
                    if (String.valueOf(response.code()).equals("404")) {
                        Toast.makeText(getActivity(), "School ID not exists", Toast.LENGTH_SHORT).show();


                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("GRADE_ERROR*", "" + t.toString());

            }
        });
    }


    @Override
    public void onClick(View view) {
        assert getActivity() != null;

        switch (view.getId()) {
            case R.id.btnUpdate:
                if (arrayList.size() > 0) {

                    JSONObject objectGrade = new JSONObject();

                    JSONObject objectOrderBy = new JSONObject();
                    for (int i = 0; i < arrayList.size(); i++) {

                        JSONObject object = new JSONObject();

                        try {
                            object.put("grade_name", arrayList.get(i).getGrade());
                            object.put("from_mark", arrayList.get(i).getMinMarks());
                            object.put("to_mark", arrayList.get(i).getMaxMarks());
                            object.put("status", "true");

                            objectOrderBy.put(String.valueOf(i + 1), object);

                            objectGrade.put("grade", objectOrderBy);

                        } catch (JSONException je) {

                        }

                    }

                    updateGradeAPI(objectGrade, SubfromDate);
                }

                break;

            case R.id.button_added:
                if (arrayList.size() > 0) {

                    JSONObject objectGrade = new JSONObject();

                    JSONObject objectOrderBy = new JSONObject();
                    for (int i = 0; i < arrayList.size(); i++) {

                        JSONObject object = new JSONObject();

                        try {
                            object.put("grade_name", arrayList.get(i).getGrade());
                            object.put("from_mark", arrayList.get(i).getMinMarks());
                            object.put("to_mark", arrayList.get(i).getMaxMarks());
                            object.put("status", "true");

                            objectOrderBy.put(String.valueOf(i + 1), object);

                            objectGrade.put("grade", objectOrderBy);

                        } catch (JSONException je) {

                        }

                    }

                    addGradeAPI(objectGrade, SubfromDate);


                }

                break;

            case R.id.iv_backBtn:
                Intent intent= new Intent(getActivity(),ExamActivity.class);
                intent.putExtra("Assessement_Option","Exam");
                getActivity().startActivity(intent);
                break;

            case R.id.tv_App:

                str_grade = "A++";
                openDialog();
                break;

            case R.id.tv_Ap:
                str_grade = "A+";
                boolean a = true;
                if (arrayList.size() > 0) {

                    for (int i = 0; i < arrayList.size(); i++) {

                        if (arrayList.get(i).getGrade().equals(str_grade)) {
                            a = false;
                            tv_Ap.setBackgroundColor(Color.parseColor("#72d548"));

                        }
                    }

                }


                openDialog();
                break;

            case R.id.tv_A:
                str_grade = "A";
                openDialog();
                break;

            case R.id.tv_B_plus:
                str_grade = "B+";
                openDialog();
                break;

            case R.id.tv_B:
                str_grade = "B";
                openDialog();
                break;

            case R.id.tv_C:
                str_grade = "C";
                openDialog();
                break;

            case R.id.tv_D:
                str_grade = "D";
                openDialog();
                break;

        }
    }

    private void updateGradeAPI(JSONObject objectGrade, String subfromDate) {
        mApiService.updateGradeBarrier(Constant.SCHOOL_ID, objectGrade, Constant.EMPLOYEE_BY_ID, subfromDate)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("Grade_Response_Update", "" + response.body());
                        if (response.isSuccessful()) {

                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    Log.i("update grade ","" + response.body());

                                    //getGradeBarriers();

                                    setRecyclerViw();
                                } else {
                                    Toast.makeText(getActivity(), "Data already added", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            try {
                                assert response.errorBody()!=null;
                                JSONObject object = new JSONObject(response.errorBody().string());
                                String message = object.getString("message");
                                Log.d("ADMIN_API", message);
                              //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.i("Grade_Response_ERROR", "" + t.toString());
                    }
                });
    }


    private void openDialog() {

        assert getActivity() != null;

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.assesment_dialog);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_grade = dialog.findViewById(R.id.tv_grade);
        final EditText et_fromMarks = dialog.findViewById(R.id.et_fromMarks);
        final EditText et_ToMarks = dialog.findViewById(R.id.et_ToMarks);
        TextView tv_add = dialog.findViewById(R.id.tv_add);

        if (!str_grade.equals(""))
            tv_grade.setText(str_grade);

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_minMarks = Long.parseLong(et_fromMarks.getText().toString());
                str_maxMarks = Long.parseLong(et_ToMarks.getText().toString());

                if (str_grade.equals("")) {
                    Toast.makeText(getActivity(), "Grade is required", Toast.LENGTH_SHORT).show();

                } else if (et_fromMarks.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Minimum Marks Is Required", Toast.LENGTH_SHORT).show();

                } else if (et_ToMarks.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Maximum Marks Is Required", Toast.LENGTH_SHORT).show();

                } else if (str_minMarks > str_maxMarks) {
                        Toast.makeText(getActivity(), "max is greater than min", Toast.LENGTH_SHORT).show();
                } else {

                        boolean b = true;

                        if (arrayList.size() > 0) {

                            for (int i = 0; i < arrayList.size(); i++) {

                                if (arrayList.get(i).getGrade().equals(str_grade)) {
                                    b = false;
                                    arrayList.set(i, new AssesmentModel(str_grade, str_minMarks, str_maxMarks));

                                }
                            }

                        }

                        if (b) {
                            gradeSave();
                            arrayList.add(new AssesmentModel(str_grade, str_minMarks, str_maxMarks));

                        } else {
                            Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                        }

                        setRecyclerViw();
                        dialog.dismiss();

                    }

                }

        });
    }

    private void gradeSave() {

        JSONObject objectOrderBy = new JSONObject();
        JSONObject object = new JSONObject();
        JSONObject objectGrade = new JSONObject();


        try {
            object.put("grade_name", str_grade);
            object.put("from_mark", str_minMarks);
            object.put("to_mark", str_maxMarks);
            object.put("status", "true");

            objectOrderBy.put("1", object);

            objectGrade.put("grade", objectOrderBy);

        } catch (JSONException je) {

        }
        Log.i("GradeData*", "" + objectGrade);

        //  addGradeAPI(objectGrade);

    }

    private void addGradeAPI(JSONObject objectGrade, String subfromDate) {
        mApiService.addGradeBarrier(Constant.SCHOOL_ID, objectGrade, Constant.EMPLOYEE_BY_ID, subfromDate)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("Grade_Response_Add", "" + response.body()+response.code());
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    Log.i("Grade_Response", "" + response.body());

                                    //getGradeBarriers();

                                    setRecyclerViw();
                                } else {
                                    Toast.makeText(getActivity(), "Data already added", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            try {
                                assert response.errorBody()!=null;
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
                        Log.i("Grade_Response_ERROR", "" + t.toString());
                    }
                });
    }

    private void setRecyclerViw() {

        AssessmentAdapter adapter = new AssessmentAdapter(arrayList, getActivity(), Constant.RV_ASSESSMENT_GRADE_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}
