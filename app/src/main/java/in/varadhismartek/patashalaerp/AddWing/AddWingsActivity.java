package in.varadhismartek.patashalaerp.AddWing;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class AddWingsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etAddWings;
    ImageView ivAddWings, ivSendWings, ivBack;
    RecyclerView rcvAddWings;
    int spanCount = 2;
    private ArrayList<AddWingsModel> addWingsModelArrayList;
    AddWingsAdapter addWingsAdapter;
    APIService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wings);
        intializeViews();
        intializeListners();
        getWings();

    }

    private void getWings() {

        mApiService.gettingWings(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                        String status = (String) json1.get("status");
                        if (status.equalsIgnoreCase("Success")) {

                            JSONObject jsonObject1 = json1.getJSONObject("data");
                            Iterator<String> keys = jsonObject1.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();

                                JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                String wing_name = jsonObjectValue.getString("wing_name");
                                boolean wings_status = jsonObjectValue.getBoolean("status");


                                addWingsModelArrayList.add(new AddWingsModel(wing_name, wings_status));
                                addWingsAdapter = new AddWingsAdapter(addWingsModelArrayList,
                                        AddWingsActivity.this, ivSendWings);
                                rcvAddWings.setAdapter(addWingsAdapter);

                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(AddWingsActivity.this,"No Data", LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void intializeListners() {
        ivAddWings.setOnClickListener(this);
        ivSendWings.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    private void intializeViews() {

        etAddWings = findViewById(R.id.etAddWings);
        ivAddWings = findViewById(R.id.ivAddWings);
        ivSendWings = findViewById(R.id.iv_sendAddWings);
        rcvAddWings = findViewById(R.id.rcvAddWings);
        ivBack = findViewById(R.id.iv_backBtn);
        mApiService = ApiUtils.getAPIService();

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, spanCount);
        rcvAddWings.setLayoutManager(linearLayoutManager);
        rcvAddWings.setHasFixedSize(true);
        addWingsModelArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivAddWings:
                String value = "";
                String editvalue = etAddWings.getText().toString();
                if (editvalue.equals("")) {
                    Toast.makeText(this, "Please enter the Value", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } else {
                    if (addWingsModelArrayList.size()>0){
                        for (int i = 0; i < addWingsModelArrayList.size(); i++) {
                            if (addWingsModelArrayList.get(i).getWingsName().toLowerCase().equals(editvalue.toLowerCase())) {
                                value = addWingsModelArrayList.get(i).getWingsName();
                            }
                        }
                        if (value.toLowerCase().equals(editvalue.toLowerCase())) {
                            Toast.makeText(this, "Already Exist", LENGTH_SHORT).show();
                        } else {
                            addWingsAdapter.newValues(editvalue);
                        }

                        etAddWings.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }else {

                        addWingsAdapter = new AddWingsAdapter(addWingsModelArrayList, AddWingsActivity.this, ivSendWings);
                        rcvAddWings.setAdapter(addWingsAdapter);
                        addWingsAdapter.newValues(editvalue);
                        etAddWings.setText("");
                    }

                }


                break;

            case R.id.iv_backBtn:
                onBackPressed();
                break;


        }
    }


}
