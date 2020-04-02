package in.varadhismartek.patashalaerp.DashboardModule.UpdateActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.DashboardModule.DashboardSettingActivity;
import in.varadhismartek.patashalaerp.DashboardModule.UpdateAdapter.UpdateDivisionAdapter;
import in.varadhismartek.patashalaerp.DivisionModule.ClassSectionAndDivisionModel;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDivisionActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_backBtn, iv_sendAddDivision;
    private EditText edit_enter;
    private ImageButton add_image;
    private RecyclerView recycler_view;
    Button button_added, bt_select_all;
    APIService mApiService;
    Context context;
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<ClassSectionAndDivisionModel> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_division);
        initViews();
        initListeners();
        //setRecyclerView();
        getDivisionApi();
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        add_image.setOnClickListener(this);
        iv_sendAddDivision.setOnClickListener(this);

    }

    private void initViews() {

        mApiService = ApiUtils.getAPIService();

        iv_backBtn = findViewById(R.id.iv_backBtn);
        iv_sendAddDivision = findViewById(R.id.iv_sendAddDivision);
        iv_sendAddDivision.setVisibility(View.GONE);
        edit_enter = findViewById(R.id.edit_enter);
        add_image = findViewById(R.id.add_image);
        recycler_view = findViewById(R.id.recycler_view);

        button_added = findViewById(R.id.button_added);
        bt_select_all = findViewById(R.id.bt_select_all);

        arrayList = new ArrayList<>();
        statusList.clear();
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add_image:

                String editvalue = edit_enter.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(UpdateDivisionActivity.this, "Please enter the Value", Toast.LENGTH_SHORT).show();
                } else {


                    if (arrayList.size() > 0) {
                        boolean b = true;
                        for (int i = 0; i < arrayList.size(); i++) {
                            if ((arrayList.get(i).getName()).contains(editvalue)) {
                                b = false;
                                Toast.makeText(UpdateDivisionActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (b) {
                            arrayList.add(new ClassSectionAndDivisionModel(editvalue, true));

                        }
                    }
                    else {
                        arrayList.add(new ClassSectionAndDivisionModel(editvalue, true));
                    }



                    setRecyclerView();
                    InputMethodManager imm = (InputMethodManager) UpdateDivisionActivity.this.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;

            case R.id.iv_backBtn:

                Intent intent = new Intent(UpdateDivisionActivity.this, DashboardSettingActivity.class);
                startActivity(intent);
                finish();

                break;

        }

    }

    private void setRecyclerView() {

        UpdateDivisionAdapter adapter = new UpdateDivisionAdapter(arrayList, UpdateDivisionActivity.this,
                button_added, Constant.RV_DIVISION_CARD, bt_select_all);
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        edit_enter.setText("");

    }


    private void getDivisionApi() {

        mApiService.getDivisions(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    Log.i("DIVISION**GET", "" + response.body());
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = object.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);

                                String added_datetime = object1.getString("added_datetime");
                                String Id = object1.getString("id");
                                boolean statusDivision = object1.getBoolean("status");
                                // if (statusDivision){
                                String division = object1.getString("division");
                                String school_id = object1.getString("school_id");
                                String added_by_id = object1.getString("added_by_id");

                                Log.d("MY_DATA", added_datetime + " " + Id + " " + statusDivision + " " +
                                        division + " " + school_id + " " + added_by_id);
                                statusList.add(String.valueOf(statusDivision));
                                arrayList.add(new ClassSectionAndDivisionModel(division, statusDivision));
                                //  }




                            }
                            if (arrayList.size() == statusList.size()) {
                                bt_select_all.setText("Unselect All");
                                bt_select_all.setBackgroundResource(R.drawable.add_back);
                                bt_select_all.setTextColor(Color.WHITE);
                            }
                            setRecyclerView();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(UpdateDivisionActivity.this, "No Data", Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("GET_DIVISION_EXCEPTION", t.getMessage());

            }
        });

    }

}
