package in.varadhismartek.patashalaerp.DashboardModule.FeesModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Fragment_FeeList extends Fragment implements View.OnClickListener {
    APIService apiService;
    ArrayList<FeesModle> feesModles = new ArrayList<>();
    RecyclerView recycler_view;
    String strFeeType = "", steFeeCode = "", strInstallment = "", strDueDate = "";
    String strSerialNo = "0";
    String str_toDate = "", startYear = "", endYear = "", sDate = "", eDate = "", strSelectSession = "", SubfromDate = "";
    ArrayList<String> sessionList, spinnerList, spinnerDateList;
    Spinner spn_AcdamicYear;
    TextView tvTitle;
    FloatingActionButton fab;

    public Fragment_FeeList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fees_activity, container, false);
        apiService = ApiUtils.getAPIService();
        recycler_view = view.findViewById(R.id.recycler_view);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("Fee Structure");




        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        getFeeMethod();
        return view;

    }

    private void getFeeMethod() {
        apiService.getFeeStructure(Constant.SCHOOL_ID, "2019-12-22")
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        if (response.isSuccessful()) {
                            feesModles.clear();
                            Log.i("GET_Fees**", "" + response.body() + "r**" + response.code());
                            try {
                                JSONObject json1 = (new JSONObject(new Gson().toJson(response.body())));
                                String status = (String) json1.get("status");
                                if (status.equalsIgnoreCase("Success")) {
                                    JSONObject jsonObject1 = json1.getJSONObject("data");
                                    Iterator<String> keys = jsonObject1.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);
                                        Log.d("jsonSliderValue", jsonObjectValue.toString());

                                        strSerialNo = jsonObjectValue.getString("session");
                                        strFeeType = jsonObjectValue.getString("fee_type");
                                        steFeeCode = jsonObjectValue.getString("fee_code");
                                        strInstallment = jsonObjectValue.getString("installment");
                                        strDueDate = jsonObjectValue.getString("due_date");

                                        feesModles.add(new FeesModle(strSerialNo, strFeeType, steFeeCode, strInstallment, strDueDate));

                                    }
                                }

                                setRecyclerView(feesModles);
                            } catch (JSONException je) {

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    private void setRecyclerView(ArrayList<FeesModle> feesModles) {
        FeeStructureAdapter adapter = new FeeStructureAdapter(feesModles, getActivity(), Constant.RV_FEES_ROW);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AddFeeStructure.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Constant.FEE_LIST_SIZE=feesModles.size();
        startActivity(intent);
    }
}
