package in.varadhismartek.patashalaerp.DashboardModule.TransportModule;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import in.varadhismartek.Utils.Constant;
import in.varadhismartek.Utils.Utilities;
import in.varadhismartek.patashalaerp.Birthday.BirthdayAdapter;
import in.varadhismartek.patashalaerp.GeneralClass.CustomSpinnerAdapter;
import in.varadhismartek.patashalaerp.R;
import in.varadhismartek.patashalaerp.Retrofit.APIService;
import in.varadhismartek.patashalaerp.Retrofit.ApiUtilsPatashala;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportVechileDetails extends Fragment implements View.OnClickListener {
    APIService apiServicePatashala;
    String strVehicleUUID = "", strVehicleID = "";
    Utilities utilities;
    ImageView iv_backBtn,iv_vehicleImage, ivNoRecords;
    ArrayList<RouteList> routeLists = new ArrayList<>();
    ArrayList<RouteList> vehicleStopModules = new ArrayList<>();
    ArrayList<String> routeNumber = new ArrayList<>();
    Spinner sp_RouteNO;

    String latitudePoint, locationManually, stopImage, approxStopTime, longitudePoint,
            stopNumber, stopName, stopDistance;
    RecyclerView recyclerView;
    TransportAdapter transBusListAdapter;

    public TransportVechileDetails() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_vechile_details, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initViews(view);
        initListeners();
        getVehicleDetails();

        return view;
    }


    private void initListeners() {
        iv_backBtn.setOnClickListener(this);
    }

    private void initViews(View view) {
        apiServicePatashala = ApiUtilsPatashala.getService();

        iv_vehicleImage = view.findViewById(R.id.iv_vehicleImage);
        sp_RouteNO = view.findViewById(R.id.sp_RouteNO);
        recyclerView = view.findViewById(R.id.recyclerView);
        ivNoRecords = view.findViewById(R.id.ivNoRecords);
        iv_backBtn = view.findViewById(R.id.iv_backBtn);

        routeLists.clear();
        vehicleStopModules.clear();

        Bundle bundle = getArguments();
        if (bundle != null) {
            strVehicleUUID = bundle.getString("VEHICLE_UUID");
            strVehicleID = bundle.getString("VEHICLE_ID");
            routeLists = (ArrayList<RouteList>) bundle.getSerializable("VEHICLE_ROUTELIST");
            Log.i("routeLists****", "" + routeLists.size());


        }
        if (routeLists.size() > 0) {
            routeNumber.clear();
            for (int i = 0; i < routeLists.size(); i++) {
                String strRouteNo = routeLists.get(i).getRoute_number();
                routeNumber.add(strRouteNo);
            }
            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), routeNumber, "");
            sp_RouteNO.setAdapter(customSpinnerAdapter);

            sp_RouteNO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String strSelectRoute = sp_RouteNO.getItemAtPosition(position).toString();
                    int strSelectRouteID = (int) sp_RouteNO.getItemIdAtPosition(position);
                    Log.i("RouteID==", "" + strSelectRoute + "**" + strSelectRouteID + "**" + routeLists.get(strSelectRouteID).getRoute_uuid());

                    CallRouteDetailsAPI(routeLists.get(strSelectRouteID).getRoute_uuid());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    private void CallRouteDetailsAPI(String route_uuid) {
        utilities.displayProgressDialog(getActivity(), "Loading...", false);
        apiServicePatashala.getRouteDetails(Constant.SCHOOL_ID, route_uuid).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {

                    vehicleStopModules.clear();
                    try {

                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("ADMIN_API_object", object.toString());
                        String status = (String) object.get("status");

                        if (status.equalsIgnoreCase("success")) {

                            JSONObject jsonObject1 = object.getJSONObject("data");

                            if (jsonObject1.getJSONObject("stop_details").toString().equals("{}")) {


                            } else {
                                JSONObject jsonStop = jsonObject1.getJSONObject("stop_details");

                                Log.d("ADMIN_API_object1", jsonObject1.toString());
                                Log.d("ADMIN_API_object12", jsonStop.toString());

                                Iterator<String> keys = jsonStop.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject stopData = jsonStop.getJSONObject(key);
                                    Log.d("ADMIN_API_object123", stopData.toString());


                                    if (stopData.isNull("latitude_point")) {
                                        latitudePoint = "";
                                    } else {
                                        latitudePoint = stopData.getString("latitude_point");
                                    }

                                    if (stopData.isNull("enter_location_manually")) {
                                        locationManually = "";
                                    } else {
                                        locationManually = stopData.getString("enter_location_manually");
                                    }

                                    if (stopData.isNull("stop_image")) {
                                        stopImage = "";
                                    } else {
                                        stopImage = stopData.getString("stop_image");
                                    }

                                    if (stopData.isNull("approx_stop_time")) {
                                        approxStopTime = "";
                                    } else {
                                        approxStopTime = stopData.getString("approx_stop_time");
                                    }

                                    if (stopData.isNull("longitude_point")) {
                                        longitudePoint = "";
                                    } else {
                                        longitudePoint = stopData.getString("longitude_point");
                                    }
                                    if (stopData.isNull("stop_number")) {
                                        stopNumber = "";
                                    } else {
                                        stopNumber = stopData.getLong("stop_number") + "";
                                    }
                                    if (stopData.isNull("stop_name")) {
                                        stopName = "";
                                    } else {
                                        stopName = stopData.getString("stop_name");
                                    }
                                    if (stopData.isNull("stop_distance")) {
                                        stopDistance = "";
                                    } else {
                                        stopDistance = stopData.getString("stop_distance");
                                    }

                                    vehicleStopModules.add(new RouteList(latitudePoint, locationManually, stopImage, approxStopTime, longitudePoint,
                                            stopNumber, stopName, stopDistance));
                                }


                            }

                            if (vehicleStopModules.size() > 0) {
                                utilities.cancelProgressDialog();
                                recyclerView.setVisibility(View.VISIBLE);
                                ivNoRecords.setVisibility(View.GONE);
                               setStopRecyclerView(vehicleStopModules);
                            } else {
                                utilities.cancelProgressDialog();
                                ivNoRecords.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            }


                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
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

    private void setStopRecyclerView(ArrayList<RouteList> vehicleStopModules) {

        transBusListAdapter = new TransportAdapter( vehicleStopModules, getActivity(),Constant.RV_VEHICLE_STOP_ROW);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transBusListAdapter);
        transBusListAdapter.notifyDataSetChanged();
    }

    private void getVehicleDetails() {
        utilities.displayProgressDialog(getActivity(), "Please wait ...", false);
        apiServicePatashala.getSchoolBusDetails(Constant.SCHOOL_ID, strVehicleUUID).enqueue(new Callback<Object>() {
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


                            } else {
                                JSONObject jsonObject1 = object.getJSONObject("data");
                                Iterator<String> keys = jsonObject1.keys();

                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    JSONObject jsonVehicleDetails = jsonObject1.getJSONObject("vehicle_details");


                                    JSONArray jsonArrayVehicleImage = jsonObject1.getJSONArray("vehicle_image");
                                    Log.i("strVehicleImage**12", "" + jsonArrayVehicleImage.toString());

                                    for (int i = 0; i < jsonArrayVehicleImage.length(); i++) {

                                        String strVehicleImage = jsonArrayVehicleImage.get(i).toString();
                                        Log.i("strVehicleImage**", "" + strVehicleImage);


                                        if (!strVehicleImage.equals("")) {
                                            Glide.with(getActivity()).load(Constant.IMAGE_URL + strVehicleImage)
                                                    .into(iv_vehicleImage);
                                        }

                                    }


                                }

                            }
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
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
                Log.i("ERROR**", "" + t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        TransportLandingActivity transportActivity = (TransportLandingActivity)getActivity();
        transportActivity.loadFragment(Constant.TRANSPORT_INBOX_FRAGMENT, null);
    }
}
