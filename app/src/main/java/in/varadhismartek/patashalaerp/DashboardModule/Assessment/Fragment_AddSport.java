package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.AddWing.AddWingsModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_AddSport extends Fragment implements View.OnClickListener {
    ImageView iv_backBtn;
    EditText et_sports_name, et_player_count;
    TextView tv_add;
    RecyclerView recycler_view;
    ArrayList<AddWingsModel> arrayList;
    APIService apiService;
    String str_sports_name = "", str_player_count = "";

    public Fragment_AddSport() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_sports, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViews(view);
        initListeners();
        getSport();
        return view;
    }

    private void getSport() {
        arrayList.clear();
        apiService.getSports(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Sports**", "" + response.body());
                Log.i("Sports**", "" + Constant.SCHOOL_ID);

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

                                // if (gradeStatus) {
                                String sportsName = jsonObjectValue.getString("sport_name");

                                boolean sportsStatus = jsonObjectValue.getBoolean("status");

                                arrayList.add(new AddWingsModel(sportsName, sportsStatus));
                                // }

                            }
                            setRecyclerView();

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

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }

    private void initViews(View view) {
        apiService = ApiUtils.getAPIService();
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        et_sports_name = view.findViewById(R.id.et_sports_name);
        et_player_count = view.findViewById(R.id.et_player_count);
        tv_add = view.findViewById(R.id.tv_add);
        recycler_view = view.findViewById(R.id.recycler_view);

        arrayList = new ArrayList<>();


    }

    @Override
    public void onClick(View view) {
        assert getActivity() != null;

        switch (view.getId()) {

            case R.id.iv_backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.tv_add:

                str_sports_name = et_sports_name.getText().toString();
                //  str_player_count = et_player_count.getText().toString();

                if (str_sports_name.equals("")) {
                    Toast.makeText(getActivity(), "Sports Name Is Required", Toast.LENGTH_SHORT).show();

                } /*else if (str_player_count.equals("")) {
                    Toast.makeText(getActivity(), "Players Is Required", Toast.LENGTH_SHORT).show();
                }*/ else {


                    if (arrayList.size() > 0){
                        boolean b = true;
                        for (int i = 0; i < arrayList.size(); i++) {
                            if ((arrayList.get(i).getWingsName()).equalsIgnoreCase(str_sports_name)) {
                                b = false;
                                Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (b) {
                            arrayList.add(new AddWingsModel(str_sports_name, true));

                        }

                    }else {
                        arrayList.add(new AddWingsModel(str_sports_name, true));
                    }



                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    gradeSave();
                    /*boolean b = true;

                    if (arrayList.size() > 0) {
                        Log.i("arrayList**","1"+arrayList.size());

                        for (int i = 0; i < arrayList.size(); i++) {

                            if (arrayList.get(i).getWingsName().contains(str_sports_name)) {

                                b = false;
                                arrayList.add(new AddWingsModel(str_sports_name, true));
                                Log.i("arrayList**","78"+arrayList.get(i).getWingsName());

                            }
                        }

                    }

                    if (b) {

                        Log.i("arrayList**","3"+arrayList.size());
                        gradeSave();
                        arrayList.add(new AddWingsModel(str_sports_name, true));

                    } else {
                        Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();

                    }

                    setRecyclerView();*/
                }


                break;

        }
    }




    private void gradeSave() {
        Log.i("arrayList**","4"+arrayList.size());
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(str_sports_name);


        Log.i("GradeData*", "" + jsonArray);
        addSportsAPI(jsonArray);

    }

    private void addSportsAPI(JSONArray jsonArray) {
        apiService.addSports(Constant.SCHOOL_ID, jsonArray, Constant.EMPLOYEE_BY_ID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.i("ADD SPORT**", "" + response.body());
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Sport added successfully", Toast.LENGTH_SHORT).show();
                            et_sports_name.setText("");
                            getSport();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }

    private void setRecyclerView() {

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

        AssessmentAdapter adapter = new AssessmentAdapter(getActivity(), Constant.RV_ASSESSMENT_SPORTS_ROW, arrayList);
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}

