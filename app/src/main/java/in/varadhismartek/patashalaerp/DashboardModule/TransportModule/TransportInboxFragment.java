package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.Birthday.BirthdayAdapter;
import in.varadhismartek.patashalaerp.Birthday.BirthdayModel;
import in.varadhismartek.patashalaerp.DashboardModule.DashBoardApiAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportInboxFragment extends Fragment implements View.OnClickListener {
    APIService apiServicePatashala;
    RecyclerView recyclerView;
    ImageView iv_backBtn;
    ArrayList<RouteList> transportBusList = new ArrayList<>();
    ArrayList<RouteList>routeLists = new ArrayList<>();

    String type_of_body, GPS_Details, seating_capacity, registration_no,
            class_of_vehicle, vehicle_uuid, vehicle_id, added_datetime, route_uuid, route_number;
    Utilities utilities;
    BirthdayAdapter transBusListAdapter;
    FloatingActionButton fab;

    public TransportInboxFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transport_inbox, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListeners();
        getSchoolBusList();
        return view;


    }

    private void getSchoolBusList() {
        utilities.displayProgressDialog(getActivity(), "Please wait ...", false);
        transportBusList.clear();
        apiServicePatashala.getSchoolBusList(Constant.SCHOOL_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    utilities.cancelProgressDialog();

                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {
                            if (object.getJSONObject("data").toString().equals("{}")) {
                                utilities.cancelProgressDialog();
                                transportBusList.clear();
                                transBusListAdapter.notifyDataSetChanged();

                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("data");
                                Iterator<String> keys = jsonObject1.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonObjectValue = jsonObject1.getJSONObject(key);


                                    if (!jsonObjectValue.isNull("type_of_body")) {
                                        type_of_body = jsonObjectValue.getString("type_of_body");
                                    }
                                    if (!jsonObjectValue.isNull("GPS_Details")) {
                                        GPS_Details = jsonObjectValue.getString("GPS_Details");
                                    }
                                    if (!jsonObjectValue.isNull("seating_capacity")) {
                                        seating_capacity = jsonObjectValue.getString("seating_capacity");
                                    }
                                    if (!jsonObjectValue.isNull("registration_no")) {
                                        registration_no = jsonObjectValue.getString("registration_no");
                                    }
                                    if (!jsonObjectValue.isNull("class_of_vehicle")) {
                                        class_of_vehicle = jsonObjectValue.getString("class_of_vehicle");
                                    }
                                    if (!jsonObjectValue.isNull("vehicle_uuid")) {
                                        vehicle_uuid = jsonObjectValue.getString("vehicle_uuid");
                                    }
                                    if (!jsonObjectValue.isNull("vehicle_id")) {
                                        vehicle_id = jsonObjectValue.getString("vehicle_id");
                                    }

                                    if (!jsonObjectValue.isNull("added_datetime")) {
                                        added_datetime = jsonObjectValue.getString("added_datetime");
                                    }

                                    JSONArray jsonArray = jsonObjectValue.getJSONArray("routes");
                                    Log.i("jsonArray", "" + jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject routeJson = jsonArray.getJSONObject(i);

                                        if (routeJson.isNull("route_uuid")) {
                                            route_uuid = "NA";
                                        } else {
                                            route_uuid = routeJson.getString("route_uuid");
                                        }
                                        Log.i("route_uuid", "" + route_uuid);
                                        if (routeJson.isNull("route_number")) {
                                            route_number = "NA";
                                        } else {
                                            route_number = routeJson.getString("route_number");
                                        }
                                        Log.i("route_uuid12", "" + route_number);

                                        routeLists.add(new RouteList(route_uuid, route_number));

                                    }


                                    transportBusList.add(new RouteList(type_of_body, GPS_Details, seating_capacity, registration_no,
                                            class_of_vehicle, vehicle_uuid, vehicle_id, added_datetime,routeLists));
                                }

                                Gson gson = new Gson();
                                Log.i("goooo",""+ gson.toJson(transportBusList));
                                setRecyclerView();

                            }
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                        utilities.cancelProgressDialog();
                    }




                } else {
                    try {
                        assert response.errorBody() != null;
                        utilities.cancelProgressDialog();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String message = object.getString("message");
                        Log.d("ADMIN_API", message);
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                utilities.cancelProgressDialog();

            }
        });
    }

    private void setRecyclerView() {
        TransportAdapter transBusListAdapter = new TransportAdapter(transportBusList, getActivity(), Constant.RV_TRANSPORT_BUS_LIST_ROW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transBusListAdapter);
        transBusListAdapter.notifyDataSetChanged();
    }

    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void initViews(View view) {
        apiServicePatashala = ApiUtilsPatashala.getService();
        recyclerView = view.findViewById(R.id.recyclerView);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);
        fab = view.findViewById(R.id.fab);

    }

    @Override
    public void onClick(View v) {

        assert getActivity()!=null;

        switch (v.getId()) {
            case R.id.iv_backBtn:
                getActivity().finish();
                break;

            case R.id.fab:
                TransportLandingActivity transportActivity = (TransportLandingActivity) getActivity();
                transportActivity.loadFragment(Constant.ADD_VEHICLE_FRAGMENT, null);
                break;
        }
    }
}
