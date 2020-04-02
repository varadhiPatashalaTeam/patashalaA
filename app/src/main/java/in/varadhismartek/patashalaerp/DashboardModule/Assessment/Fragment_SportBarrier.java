package in.varadhismartek.patashalaerp.DashboardModule.Assessment;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SportBarrier extends Fragment implements View.OnClickListener {
    APIService apiService;
    private Spinner spinnerSport;
    private ArrayList<String> sportArrayList;
    private ArrayList<AssesmentModel> arrayListSport;
    String sportsName, mentors = "", mentor_position_name = "", max_players = "", support_players = "", sport_name = "", strSport;
    private RecyclerView recyclerView;
    private TextView tvAdd;
    private EditText edplayerTeam, edSupportPlayer, edMentor, edRoleName;
    private ImageView iv_backBtn;

    public Fragment_SportBarrier() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport_barrier, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initview(view);
        getSportAPI();
        getSportBarrierAPI();
        return view;

    }

    private void initview(View view) {
        apiService = ApiUtils.getAPIService();
        spinnerSport = view.findViewById(R.id.spSport);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvAdd = view.findViewById(R.id.tvAdd);

        edplayerTeam = view.findViewById(R.id.ed_playerteam);
        edSupportPlayer = view.findViewById(R.id.ed_supportplayer);
        edMentor = view.findViewById(R.id.ed_mentor);
        edRoleName = view.findViewById(R.id.ed_rolename);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        tvAdd.setOnClickListener(this);
        iv_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        sportArrayList = new ArrayList<>();
        arrayListSport = new ArrayList<>();

    }

    private void getSportAPI() {
        apiService.getSports(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.i("ADDGrade_Response", "" + response.body());
                            sportArrayList.clear();
                            sportArrayList.add("Select Sport");
                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);

                                boolean sportsStatus = jsonObjectValue.getBoolean("status");

                                if (sportsStatus) {
                                    String sportsName = jsonObjectValue.getString("sport_name");

                                    sportArrayList.add(sportsName);
                                }

                            }
                            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), sportArrayList);
                            spinnerSport.setAdapter(customSpinnerAdapter);


                        }
                    } catch (JSONException je) {

                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        spinnerSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSport = parent.getItemAtPosition(position).toString();
                Log.i("strSport***", "" + strSport);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getSportBarrierAPI() {
        apiService.getSportBarriers(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (response.isSuccessful()) {
                    Log.i("SPORTBARRIER_GET", "" + response.body());
                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");

                        if (status.equalsIgnoreCase("Success")) {
                            Log.i("SPORTBARRIER_GET**12", "" + response.body());
                            arrayListSport.clear();

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);

                                boolean sportsStatus = jsonObjectValue.getBoolean("status");
                                // if (sportsStatus) {
                                Log.i("sportsStatus**12", "" + sportsStatus);

                                if (jsonObjectValue.isNull("mentors")) {
                                    mentors = "0";
                                } else {
                                    mentors = jsonObjectValue.getString("mentors");
                                }
                                Log.i("mentors**12", "" + mentors);

                                if (jsonObjectValue.isNull("mentor_position_name")) {
                                    mentor_position_name = "0";
                                } else {
                                    mentor_position_name = jsonObjectValue.getString("mentor_position_name");
                                }


                                if (jsonObjectValue.isNull("max_players")) {
                                    max_players = "0";
                                } else {
                                    max_players = jsonObjectValue.getString("max_players");
                                }


                                if (jsonObjectValue.isNull("support_players")) {
                                    support_players = "0";
                                } else {
                                    support_players = jsonObjectValue.getString("support_players");
                                }


                                if (jsonObjectValue.isNull("sport_name")) {
                                    sport_name = "0";
                                } else {
                                    sport_name = jsonObjectValue.getString("sport_name");

                                }

                                Log.i("arrayListSport**12", "" + sport_name);

                                if (!mentors.equals("0.0")){
                                    arrayListSport.add(new AssesmentModel(mentors, mentor_position_name, max_players,
                                            support_players, sport_name));

                                }

                            }
                            if (arrayListSport.size() > 0) {
                                setRecyclerView();
                            }

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

    private void setRecyclerView() {

        Log.i("arrayListSport**12", "" + arrayListSport);

        AssessmentAdapter adapter = new AssessmentAdapter(getActivity(), arrayListSport, Constant.RV_ASSESSMENT_SPORTS_BARRIER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {


        if (strSport.equalsIgnoreCase("Select Sport")) {
            Toast.makeText(getActivity(), "Select Sport Name", Toast.LENGTH_SHORT).show();

        } else if (edplayerTeam.getText().toString().isEmpty() || edMentor.getText().toString().isEmpty()
                || edSupportPlayer.getText().toString().isEmpty() || edRoleName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill up all the fields", Toast.LENGTH_SHORT).show();

        } else {
            Log.i("ADD_SPORTBARRIER**", "" + strSport);
            mentors = edMentor.getText().toString();
            mentor_position_name = edRoleName.getText().toString();
            support_players = edSupportPlayer.getText().toString();
            max_players = edplayerTeam.getText().toString();



             gradeSave();

            setRecyclerView();

        }
    }

    private void gradeSave() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sports_name", strSport);
            jsonObject.put("max_players", max_players);
            jsonObject.put("support_players", support_players);
            jsonObject.put("mentors", mentors);
            jsonObject.put("mentor_position_name", mentor_position_name);

            updateSportBarriers(jsonObject);
            Log.i("ADD_SPORTBARRIER**", "" + jsonObject);
        } catch (JSONException je) {

        }
    }

    private void updateSportBarriers(JSONObject jsonObject) {
        apiService.updateAddSportBarriers(Constant.SCHOOL_ID, Constant.EMPLOYEE_BY_ID, jsonObject)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        Log.i("ADD_SPORTBARRIER**", "" + response.body());
                        Log.i("ADD_SPORTBARRIER**", "" + response.code());
                        spinnerSport.setSelection(0);
                        edplayerTeam.setText("");
                        edSupportPlayer.setText("");
                        edMentor.setText("");
                        edRoleName.setText("");
                        getSportBarrierAPI();

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }
}

