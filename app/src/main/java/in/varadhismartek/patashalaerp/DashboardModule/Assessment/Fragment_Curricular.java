package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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


public class Fragment_Curricular extends Fragment implements View.OnClickListener {
    TextView tvAdd;
    EditText edMarks,edActivityName;
    //Spinner spActivityList;
    RecyclerView recyclerView;
    APIService apiService;
    private ArrayList<AssesmentModel> addCurricularList;
    ArrayList<String> activitynameList;
    AssessmentAdapter assessmentAdapter;

    String strActivity ="";
    Utilities utilities;
    private ImageView iv_backBtn;
    public Fragment_Curricular() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curricular, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView(view);
        initListener();
        setRecyclerViw();
        getActivityNameList();
        return  view;
    }


    private void initListener() {
        tvAdd.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
    }

    private void initView(View view) {
        apiService = ApiUtils.getAPIService();
        tvAdd =view.findViewById(R.id.tvAdd);
        edActivityName =view.findViewById(R.id.edActivityName);
        edMarks =view.findViewById(R.id.ed_marks);
        recyclerView =view.findViewById(R.id.recyclerView);
        iv_backBtn =view.findViewById(R.id.iv_backBtn);

        addCurricularList = new ArrayList<>();
        activitynameList = new ArrayList<>();
    }


    private void getActivityNameList() {
        utilities.displayProgressDialog(getActivity(),"Loading...",false);
        addCurricularList.clear();

        apiService.getCurricular(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();
                            Log.i("Curricular_Get", "" + jsonObject1);

                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                String activityName = jsonObjectValue.getString("cocurricular_activities");
                                String marks = String.valueOf(jsonObjectValue.getLong("marks"));
                                boolean activityStatus = jsonObjectValue.getBoolean("status");

                                if (activityStatus) {
                                    addCurricularList.add(new AssesmentModel(activityName,marks,activityStatus));

                                }

                            }
                            utilities.cancelProgressDialog();
                            setRecyclerViw() ;
                           /* assessmentAdapter = new AssessmentAdapter(Constant.RV_CURRICULAR_ROW,getActivity(),addCurricularList);

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(assessmentAdapter);
                          */  Log.i("addCurricularList", "" + addCurricularList);

                        }
                    } catch (JSONException je) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
utilities.cancelProgressDialog();
            }
        });

       /* spActivityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 strActivity = spActivityList.getItemAtPosition(position).toString();
                Log.i("strActivity**",""+strActivity);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
            case R.id.tvAdd:
                String strActivity = edActivityName.getText().toString();
                String editvalue = edMarks.getText().toString();

                if(edActivityName.getText().toString().isEmpty() || edMarks.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please fill all fields", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }else {

                    boolean b = true;

                    if (addCurricularList.size() > 0) {

                        for (int i = 0; i < addCurricularList.size(); i++) {

                            if (addCurricularList.get(i).getName().equals(strActivity)) {
                                b = false;
                                addCurricularList.set(i, new AssesmentModel(strActivity, edMarks.getText().toString(), true));

                            }
                        }

                    }

                    if (b) {
                       // saveCurricular();
                        addCurricularList.add(new AssesmentModel(strActivity, edMarks.getText().toString(), true));


                    } else {
                        Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();

                    }
                    saveCurricular();
                    setRecyclerViw();
                }
                break;
        }
    }

    private void saveCurricular() {

        JSONObject objectOrderBy = new JSONObject();
        JSONObject object = new JSONObject();


        try{
            object.put("activity_name",edActivityName.getText().toString().trim());
            object.put("marks", Long.parseLong(edMarks.getText().toString()));
            object.put("status","true");

            objectOrderBy.put("1",object);


        }catch (JSONException je){

        }


        Log.i("GradeData*",""+objectOrderBy);

        addCurricularList(objectOrderBy);



    }

    private void addCurricularList(JSONObject objectOrderBy) {
        utilities.displayProgressDialog(getActivity(),"Loading...",false);
        apiService.addCurricular(Constant.SCHOOL_ID,objectOrderBy,Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    utilities.cancelProgressDialog();
                    Toast.makeText(getActivity(), "Curricular have update successfully ",
                            Toast.LENGTH_SHORT).show();
                }
                Log.i("Add_Resp",""+Constant.SCHOOL_ID);
                Log.i("Add_Resp",""+response.body());
                Log.i("Add_Resp",""+response.code());

                edActivityName.setText("");
                edMarks.setText("");
                getActivityNameList();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
utilities.cancelProgressDialog();
            }
        });
    }

    private void setRecyclerViw() {


        assessmentAdapter = new AssessmentAdapter(Constant.RV_CURRICULAR_ROW,getActivity(),addCurricularList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(assessmentAdapter);
        Log.i("addCurricularList", "" + addCurricularList);
    }
}