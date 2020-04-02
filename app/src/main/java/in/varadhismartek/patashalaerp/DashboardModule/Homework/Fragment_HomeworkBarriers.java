package in.varadhismartek.patashalaerp.DashboardModule.Homework;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_HomeworkBarriers extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private ImageView iv_backBtn,ivSendNext;
    private SwitchCompat switchCompat;
    private Button buttonAdd;
    private LinearLayout linearDivision, linearClass, linearSelector,linearList;
    private TextView tvDivision, tvClass;
    private ArrayList<String> listDivision, listClass;
    private Spinner spinnerDivisionClass;
    private TextView tvMinusButton, tvPlusButton, edCountValues;
    private RecyclerView recyclerView;
    private int count = 1;
    private String strSpinnerValue;
    private List<HomeworkModel> homeworkModels = new ArrayList<>();

    private APIService mAPIService;
    ProgressDialog progressDialog;

    public Fragment_HomeworkBarriers() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homework_barriers_fragment, container, false);
        initViews(view);
        initListener();
        getDivisionApi();
        getHomeworkBarrierApi();

        return view;
    }

    private void initListener() {
        linearDivision.setOnClickListener(this);
        linearClass.setOnClickListener(this);
        tvPlusButton.setOnClickListener(this);
        tvMinusButton.setOnClickListener(this);
        switchCompat.setOnCheckedChangeListener(this);
        spinnerDivisionClass.setOnItemSelectedListener(this);
        buttonAdd.setOnClickListener(this);
        iv_backBtn.setOnClickListener(this);
        ivSendNext.setOnClickListener(this);
    }

    private void initViews(View view) {

        mAPIService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(getActivity());

        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        ivSendNext = view.findViewById(R.id.ivSendNext);

        linearDivision = view.findViewById(R.id.linear_division);
        linearClass = view.findViewById(R.id.linear_class);
        linearSelector = view.findViewById(R.id.linear_selector);
        spinnerDivisionClass = view.findViewById(R.id.sp_division_class);
        switchCompat = view.findViewById(R.id.switch_compat);
        buttonAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerView);
        linearList = view.findViewById(R.id.linear_list);

        tvDivision = view.findViewById(R.id.tvdivision);
        tvClass = view.findViewById(R.id.tvclass);
        tvMinusButton = view.findViewById(R.id.tv_minus);
        tvPlusButton = view.findViewById(R.id.tv_Plus);
        edCountValues = view.findViewById(R.id.ed_count);

        linearSelector.setVisibility(View.GONE);
        linearList.setVisibility(View.GONE);


        listDivision = new ArrayList<>();
        listClass = new ArrayList<>();

        listClass.clear();
        listDivision.clear();

     /*   listClass.add("UKG");
        listClass.add("LKG");
        listClass.add("1");
        listClass.add("2");
        listClass.add("3");
        listClass.add("4");*/

        loadData(listDivision);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAdd:

                if (spinnerDivisionClass.getSelectedItemPosition() == 0){
                    Toast.makeText(getActivity(), "First Select Division", Toast.LENGTH_SHORT).show();

                } else{

                    if (homeworkModels.size() > 0) {
                        boolean b = true;

                        for (int i = 0; i < homeworkModels.size(); i++) {
                            if ((homeworkModels.get(i).getDivisionClassName()).contains(strSpinnerValue)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                                //addHomeWorkBarrierApi();
                            }
                        }

                        if (b) {
                            homeworkModels.add(new HomeworkModel(strSpinnerValue, String.valueOf(count), true));
                            addHomeWorkBarrierApi();

                        }

                    } else {
                        homeworkModels.add(new HomeworkModel(strSpinnerValue, String.valueOf(count), true));
                        addHomeWorkBarrierApi();
                    }
                }

                loadRecyclerViewData(homeworkModels);


                break;
            case R.id.linear_division:

                linearDivision.setBackgroundColor(getResources().getColor(R.color.master_admin));
                linearClass.setBackgroundColor(getResources().getColor(R.color.white));
                tvDivision.setTextColor(getResources().getColor(R.color.white));
                tvClass.setTextColor(getResources().getColor(R.color.drak_grey));

                loadData(listDivision);
                break;
            case R.id.linear_class:

                linearClass.setBackgroundColor(getResources().getColor(R.color.master_admin));
                linearDivision.setBackgroundColor(getResources().getColor(R.color.white));
                tvClass.setTextColor(getResources().getColor(R.color.white));
                tvDivision.setTextColor(getResources().getColor(R.color.drak_grey));


                loadData(listClass);
                break;
            case R.id.tv_minus:
                if (count > 1) {
                    count--;
                    edCountValues.setText(String.valueOf(count));
                }
                break;
            case R.id.tv_Plus:
                if (count < 20) {
                    count++;
                    edCountValues.setText(String.valueOf(count));
                }
                break;

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;
            case R.id.ivSendNext:

               // Toast.makeText(getActivity(),"Inbox",Toast.LENGTH_SHORT).show();
                final HomeWorkActivity homeWorkActivity = (HomeWorkActivity) getActivity();
                homeWorkActivity.loadFragment(Constant.HOMEWORK_INBOX_FRAGMENT, null);




                break;
        }
    }

    private void loadRecyclerViewData(List<HomeworkModel> homeworkModels) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(getActivity(), homeworkModels,
                Constant.RV_HOMEWORK_BARRIER, homeworkInterface/*,ivSendNext*/);
        recyclerView.setAdapter(homeworkAdapter);
        homeworkAdapter.notifyDataSetChanged();

    }

    private void loadData(ArrayList<String> listDivision) {
        CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(getActivity(),listDivision);
        spinnerDivisionClass.setAdapter(dataAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            linearSelector.setVisibility(View.VISIBLE);
            linearList.setVisibility(View.VISIBLE);
        } else {
            linearSelector.setVisibility(View.GONE);
            linearList.setVisibility(View.GONE);
        }

        Log.d("Update_school_satus", String.valueOf(isChecked));


        mAPIService.updateSchoolHomeworkBarrierStatus(Constant.SCHOOL_ID, String.valueOf(isChecked),
                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){
                    assert response.body() != null;
                    Log.d("Update_school_status", String.valueOf(response.code()));
                    Log.d("Update_school_status", response.body().toString());

                    getHomeworkBarrierApi();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Log.d("Update_school_ex", t.getMessage());

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position!=0)
            strSpinnerValue = String.valueOf(spinnerDivisionClass.getItemAtPosition(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getDivisionApi() {

        listDivision.clear();
        listDivision.add(0,"- Division -");

        mAPIService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.i("DIVISION**GET", "" + response.body());
                    try { JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                boolean statusDivision = object1.getBoolean("status");
                                String division = object1.getString("division");
                                String school_id = object1.getString("school_id");
                                String added_by_id = object1.getString("added_by_id");
                                Log.d("MY_DATA", added_datetime + " " + Id + " " + statusDivision + " " + division + " " + school_id + " " + added_by_id);

                                if(statusDivision)
                                    listDivision.add(division);

                            }

                            loadData(listDivision);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());
            }
        });

        loadData(listDivision);

    }

    private void getHomeworkBarrierApi(){

        if (homeworkModels.size()>0)
            homeworkModels.clear();

        mAPIService.getHomeworkBarrier(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    assert response.body() != null;
                    Log.d("Homework_api_success", response.body().toString());
                    Log.d("Homework_api_success", String.valueOf(response.code()));

                    JSONObject obj;

                    try {
                        obj = new JSONObject(new Gson().toJson(response.body()));
                        JSONObject jsonData = obj.getJSONObject("data");
                        Log.d("Homework_api_data", jsonData.toString());

                        Iterator<String> key = jsonData.keys();
                        while (key.hasNext()){

                            String myKey = key.next();
                            Log.d("Homework_api_key", myKey);

                            JSONObject keyData = jsonData.getJSONObject(myKey);

                            String division = keyData.getString("division");
                            String number_of_homeworks = keyData.getString("number_of_homeworks");
                            //String updated_datetime = keyData.getString("updated_datetime");
                            String added_by = keyData.getString("added_by");
                            String added_datetime = keyData.getString("added_datetime");

                            Log.d("Homework_api_key_data", division+" "+number_of_homeworks+" "+" "+added_by+" "+
                                    added_datetime);

                            homeworkModels.add(new HomeworkModel(division, number_of_homeworks, true));

                        }

                        loadRecyclerViewData(homeworkModels);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {

                    Log.d("Homework_api_fail", String.valueOf(response.code()));
                    loadRecyclerViewData(homeworkModels);

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });

    }

    private void addHomeWorkBarrierApi(){
//VS017BH00002
        mAPIService.addHomeworkBarrier(Constant.SCHOOL_ID,spinnerDivisionClass.getSelectedItem().toString(),
                "true",String.valueOf(count),Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    Toast.makeText(getActivity(),"Added successfully ",Toast.LENGTH_SHORT).show();
                    Log.d("Add_MESSAGE", String.valueOf(response.code()));
                }else {

                    Log.d("Add_MESSAGE", String.valueOf(response.code()));

                }

            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });


    }

    Homework_Interface homeworkInterface = new Homework_Interface() {
        @Override
        public void selectRow(final String divisionName, String count, int position) {

            assert getActivity()!=null;

           // Toast.makeText(getActivity(), "Clicked "+position, Toast.LENGTH_SHORT).show();

           final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_update_homework);
            //noinspection ConstantConditions
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            final TextView tv_division_name = dialog.findViewById(R.id.tv_division_name);
            TextView tv_minus = dialog.findViewById(R.id.tv_minus);
            final TextView tv_count = dialog.findViewById(R.id.tv_count);
            TextView tv_Plus = dialog.findViewById(R.id.tv_Plus);
            Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
            Button bt_update = dialog.findViewById(R.id.bt_update);

            tv_division_name.setText(divisionName);
            tv_count.setText(count);

            final int[] number = {Integer.parseInt(count)};

            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (number[0] > 1){
                        number[0] = number[0] - 1;

                        tv_count.setText(String.valueOf(number[0]));

                    }
                }
            });

            tv_Plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (number[0] < 20){
                        number[0] = number[0] + 1;
                        tv_count.setText(String.valueOf(number[0]));

                    }
                }
            });

            bt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateHomeworkBarrierApi(divisionName, number[0], dialog);

                }
            });

        }
    };


    private void updateHomeworkBarrierApi(String divisionName, int number, final Dialog dialog){

        mAPIService.updateHomeworkBarrier(Constant.SCHOOL_ID,divisionName,String.valueOf(number),
                Constant.EMPLOYEE_BY_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {

                if (response.isSuccessful()){

                    Log.d("UPDATE_HOMEWORK", String.valueOf(response.code()));

                    getHomeworkBarrierApi();
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

}
